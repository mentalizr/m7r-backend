package org.mentalizr.backend.security.auth;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.mentalizr.backend.Const;
import org.mentalizr.backend.exceptions.IllegalServiceInputException;
import org.mentalizr.backend.security.session.SessionManager;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.utils.PasswordHelper;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private static final Logger authLogger = Const.authLogger;

    public static void login(HttpServletRequest httpServletRequest, String username, char[] password) throws UnauthorizedException, InfrastructureException, IllegalServiceInputException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        AssertMethodPrecondition.parameterNotNull("username", username);
        AssertMethodPrecondition.parameterNotNull("password", password);

        logger.debug("Check authentication for user = '" + username + "'");

        SessionManager.invalidate(httpServletRequest);

        checkUsernameSanity(username);
        PasswordHelper.checkPasswordSanity(password);

        UserLoginCompositeVO userLoginCompositeVO = obtainUserLoginCompositeVO(username);

        checkPasswordHash(userLoginCompositeVO, password);

        String sessionString = SessionManager.createSessionForLogin(httpServletRequest, userLoginCompositeVO);

        authLogger.info("user [" + userLoginCompositeVO.getUserLoginVO().getUsername() + "] login. " + sessionString);
    }

    public static void loginWithAccessKey(HttpServletRequest httpServletRequest, String accessKey) throws UnauthorizedException, InfrastructureException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        AssertMethodPrecondition.parameterNotNull("accessKey", accessKey);

        logger.debug("Check authentication for access key = '" + accessKey + "'");

        SessionManager.invalidate(httpServletRequest);

        checkUsernameSanity(accessKey);

        UserAccessKeyCompositeVO userAccessKeyCompositeVO = obtainUserAccessKeyCompositeVO(accessKey);

        String sessionString = SessionManager.createSessionForAccessKey(httpServletRequest, userAccessKeyCompositeVO);

        authLogger.info("accessKey [" + accessKey + "] login. " + sessionString);
    }

    public static void logout(HttpServletRequest httpServletRequest) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        
        if (SessionManager.hasSessionInAnyStaging(httpServletRequest)) {
            SessionManager.invalidatePreexisting(httpServletRequest);
        } else {
            logger.info("Logout without running session.");
        }
    }

    public static Authentication assertHasValidSession(HttpServletRequest httpServletRequest, String restService, boolean logAssertionFailure) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        try {
            return new Authentication(httpServletRequest);
        } catch (UnauthorizedException e) {
            if (logAssertionFailure) authLogger.warn("No valid session when calling REST service [" + restService + "]");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    public static void assertHasSessionInAnyStaging(HttpServletRequest httpServletRequest, String restService, boolean logAssertionFailure) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        boolean hasSession = SessionManager.hasSessionInAnyStaging(httpServletRequest);
        if (!hasSession) {
            if (logAssertionFailure) authLogger.warn("No running session in any state when calling REST service [" + restService + "].");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    private static UserLoginCompositeVO obtainUserLoginCompositeVO(String username) throws UnauthorizedException, InfrastructureException {
        try {
            return UserLoginCompositeDAO.findByUk_username(username);
        } catch (DataSourceException e) {
            throw new InfrastructureException(e);
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedException("[" + username + "] Login rejected. UserLogin unknown.");
        }
    }

    private static UserAccessKeyCompositeVO obtainUserAccessKeyCompositeVO(String accessKey) throws InfrastructureException, UnauthorizedException {
        try {
            return UserAccessKeyCompositeDAO.findByAccessKey(accessKey);
        } catch (DataSourceException e) {
            throw new InfrastructureException(e);
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

    private static void checkUsernameSanity(String username) throws UnauthorizedException {

        if (username.equals("")) {
            throw new UnauthorizedException("Login rejected due to empty username.");
        }

        if (username.length() > 50) {
            throw new UnauthorizedException("Login rejected. Username length exceeded.");
        }

        // TODO
        // Further sanity checks: illegal chars ... prevent sql injection

    }

    private static void checkPasswordSanity(char[] password) throws UnauthorizedException {

        if (password.length == 0) {
            throw new UnauthorizedException("Login rejected due to empty password.");
        }

        if (password.length > 100) {
            throw new UnauthorizedException("Login rejected. Password length exceeded.");
        }
    }

}
