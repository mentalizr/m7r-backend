package org.mentalizr.backend.accessControl;

import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import de.arthurpicht.webAccessControl.handler.LoginHandler;
import de.arthurpicht.webAccessControl.securityAttribute.SecurityAttribute;
import de.arthurpicht.webAccessControl.securityAttribute.User;
import de.arthurpicht.webAccessControl.securityAttribute.requirements.Requirements;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.mentalizr.backend.accessControl.roles.UserFactory;
import org.mentalizr.backend.utils.CredentialsSanity;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

public class M7rLoginHandler extends LoginHandler {

    public SecurityAttribute checkCredentials(String username, char[] password) throws UnauthorizedException {

        try {
            CredentialsSanity.checkUsernameSanity(username);
            CredentialsSanity.checkPasswordSanity(password);
        } catch (CredentialsSanity.BadCredentialsException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
        UserLoginCompositeVO userLoginCompositeVO = obtainUserLoginCompositeVO(username);

        checkPasswordHash(userLoginCompositeVO, password);

        User user = UserFactory.createUserForRole(userLoginCompositeVO);
        Requirements requirements = RequirementsFactory.createRequirements(userLoginCompositeVO);

        return new SecurityAttribute(user, requirements);
    }

    public SecurityAttribute checkCredentials(String accessKey) throws UnauthorizedException {

        try {
            CredentialsSanity.checkUsernameSanity(accessKey);
        } catch (CredentialsSanity.BadCredentialsException e) {
            throw new UnauthorizedException(e.getMessage(), e);
        }
        UserAccessKeyCompositeVO userAccessKeyCompositeVO = obtainUserAccessKeyCompositeVO(accessKey);

        User user = UserFactory.createUserSessionForAccessKeyUser(userAccessKeyCompositeVO);
        Requirements requirements = RequirementsFactory.createRequirements(userAccessKeyCompositeVO);

        return new SecurityAttribute(user, requirements);
    }

    private static UserLoginCompositeVO obtainUserLoginCompositeVO(String username) throws UnauthorizedException {
        try {
            return UserLoginCompositeDAO.findByUk_username(username);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedException("[" + username + "] Login rejected. UserLogin unknown.");
        }
    }

    private static UserAccessKeyCompositeVO obtainUserAccessKeyCompositeVO(String accessKey) throws UnauthorizedException {
        try {
            return UserAccessKeyCompositeDAO.findByAccessKey(accessKey);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedException("Login by access key [" + accessKey + "] rejected. Unrecognized.");
        }
    }

    private static void checkPasswordHash(UserLoginCompositeVO userLoginCompositeVO, char[] password) throws UnauthorizedException {
        Argon2 argon2 = Argon2Factory.create();
        try {
            if (!argon2.verify(userLoginCompositeVO.getUserLoginVO().getPasswordHash(), password)) {
                throw new UnauthorizedException(
                        "[" + userLoginCompositeVO.getUserLoginVO().getUsername() + "] login rejected. Wrong password."
                );
            }
        } finally {
            argon2.wipeArray(password);
        }
    }

}
