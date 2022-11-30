package org.mentalizr.backend.accessControl;

import de.arthurpicht.webAccessControl.WACContextRegistry;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import de.arthurpicht.webAccessControl.handler.LoginHandler;
import de.arthurpicht.webAccessControl.securityAttribute.SecurityAttribute;
import de.arthurpicht.webAccessControl.securityAttribute.User;
import de.arthurpicht.webAccessControl.securityAttribute.requirements.Requirements;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.mentalizr.backend.accessControl.roles.PatientAnonymous;
import org.mentalizr.backend.accessControl.roles.UserFactory;
import org.mentalizr.backend.utils.CredentialsSanity;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.slf4j.Logger;

public class M7rLoginHandler extends LoginHandler {

    private static final Logger authLogger = WACContextRegistry.getContext().getLogger();

    private static class BadPasswordHash extends Exception {
        private static final long serialVersionUID = -5562282437669888157L;
        public BadPasswordHash(String message) {
            super(message);
        }
    }

    public SecurityAttribute checkCredentials(String username, char[] password) throws UnauthorizedException {
        try {
            CredentialsSanity.checkUsernameSanity(username);
            CredentialsSanity.checkPasswordSanity(password);
            UserLoginCompositeVO userLoginCompositeVO = obtainUserLoginCompositeVOByUsername(username);

            checkPasswordHash(userLoginCompositeVO, password);

            User user = UserFactory.createLoginUserForRole(userLoginCompositeVO);
            Requirements requirements = RequirementsFactory.createRequirements(userLoginCompositeVO);

            return new SecurityAttribute(user, requirements);

        } catch (CredentialsSanity.BadCredentialsException e) {
            authLogger.warn(e.getMessage());
            throw new UnauthorizedException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            String message = "[" + username + "] Login rejected. UserLogin unknown.";
            authLogger.warn(message);
            throw new UnauthorizedException(message);
        } catch (BadPasswordHash e) {
            authLogger.warn(e.getMessage());
            throw new UnauthorizedException(e);
        }
    }

    public SecurityAttribute checkCredentials(String accessKey) throws UnauthorizedException {
        try {
            CredentialsSanity.checkUsernameSanity(accessKey);
            UserAccessKeyCompositeVO userAccessKeyCompositeVO = obtainUserAccessKeyCompositeVOByAccessKey(accessKey);

            User user = UserFactory.createAnonymousUser(userAccessKeyCompositeVO);
            Requirements requirements = RequirementsFactory.createRequirements(userAccessKeyCompositeVO);

            return new SecurityAttribute(user, requirements);

        } catch (EntityNotFoundException e) {
            String message = "Login by access key [" + accessKey + "] rejected. Unrecognized.";
            authLogger.warn(message);
            throw new UnauthorizedException(message);
        } catch (CredentialsSanity.BadCredentialsException e) {
            authLogger.warn(e.getMessage());
            throw new UnauthorizedException(e.getMessage(), e);
        }
    }

    @Override
    public SecurityAttribute refreshSecurityAttribute(SecurityAttribute securityAttribute) {

        String userId = securityAttribute.getUser().getUserId();
        User user;
        Requirements requirements;

        try {
            if (securityAttribute.isInRole(PatientAnonymous.class)) {
                UserAccessKeyCompositeVO userAccessKeyCompositeVO = obtainUserAccessKeyCompositeVOByUserId(userId);
                user = UserFactory.createAnonymousUser(userAccessKeyCompositeVO);
                requirements = RequirementsFactory.createRequirements(userAccessKeyCompositeVO);
            } else {
                UserLoginCompositeVO loginCompositeVO = obtainUserLoginCompositeVOByUserId(userId);
                user = UserFactory.createLoginUserForRole(loginCompositeVO);
                requirements = RequirementsFactory.createRequirements(loginCompositeVO);
            }
            return new SecurityAttribute(user, requirements);
        } catch (EntityNotFoundException e) {
            throw new IllegalStateException("Unknown userId [" + userId + "].");
        }
    }

    private static UserLoginCompositeVO obtainUserLoginCompositeVOByUsername(String username) throws EntityNotFoundException {
        try {
            return UserLoginCompositeDAO.findByUk_username(username);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

    private static UserLoginCompositeVO obtainUserLoginCompositeVOByUserId(String userId) throws EntityNotFoundException {
        try {
            return UserLoginCompositeDAO.load(userId);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

    private static UserAccessKeyCompositeVO obtainUserAccessKeyCompositeVOByAccessKey(String accessKey) throws EntityNotFoundException {
        try {
            return UserAccessKeyCompositeDAO.findByAccessKey(accessKey);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

    private static UserAccessKeyCompositeVO obtainUserAccessKeyCompositeVOByUserId(String userId) throws EntityNotFoundException {
        try {
            return UserAccessKeyCompositeDAO.load(userId);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkPasswordHash(UserLoginCompositeVO userLoginCompositeVO, char[] password) throws BadPasswordHash {
        Argon2 argon2 = Argon2Factory.create();
        try {
            if (!argon2.verify(userLoginCompositeVO.getUserLoginVO().getPasswordHash(), password)) {
                throw new BadPasswordHash(
                        "[" + userLoginCompositeVO.getUserLoginVO().getUsername() + "] login rejected. Wrong password."
                );
            }
        } finally {
            argon2.wipeArray(password);
        }
    }

}
