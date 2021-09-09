package org.mentalizr.backend.auth;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private static final Logger authLogger = LoggerFactory.getLogger("m7r-auth");

    public static void login(HttpServletRequest httpServletRequest, String username, char[] password) throws UnauthorizedException, InfrastructureException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        AssertMethodPrecondition.parameterNotNull("username", username);
        AssertMethodPrecondition.parameterNotNull("password", password);

        logger.debug("Check Authentication for user = '" + username + "'");

        logoutIfLoggedIn(httpServletRequest);

        checkUsernameSanity(username);
        checkPasswordSanity(password);

        UserLoginCompositeVO userLoginCompositeVO = obtainUserLoginCompositeVO(username);

        checkPasswordHash(userLoginCompositeVO, password);

        UserHttpSessionAttribute userHttpSessionAttribute = createUserSessionAttributeForRole(userLoginCompositeVO);

        createSessionWithUserSessionAttribute(httpServletRequest, userHttpSessionAttribute);

        authLogger.info("user [" + userLoginCompositeVO.getUsername() + "] login.");
    }

    public static void loginWithAccessKey(HttpServletRequest httpServletRequest, String accessKey) throws UnauthorizedException, InfrastructureException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        AssertMethodPrecondition.parameterNotNull("accessKey", accessKey);

        logger.debug("Check Authentication for access key = '" + accessKey + "'");

        logoutIfLoggedIn(httpServletRequest);

        checkUsernameSanity(accessKey);

        UserAccessKeyCompositeVO userAccessKeyCompositeVO = obtainUserAccessKeyCompositeVO(accessKey);
        UserHttpSessionAttribute userHttpSessionAttribute = createUserSessionAttributeForAccessKeyUser(userAccessKeyCompositeVO);

        createSessionWithUserSessionAttribute(httpServletRequest, userHttpSessionAttribute);
    }

    public static void logout(HttpServletRequest httpServletRequest) {

        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession != null) {

            UserHttpSessionAttribute userHttpSessionAttribute = (UserHttpSessionAttribute) httpSession.getAttribute(UserHttpSessionAttribute.USER);
            httpSession.invalidate();

            if (userHttpSessionAttribute != null) {
                authLogger.info("[" + userHttpSessionAttribute.getUserVO().getId() + "] logout");
            } else {
                logger.error("Session inconsistent: session found valid without session attribute. Unknown user logged out.");
            }

        } else {
            logger.info("logout without valid session.");
        }
    }

    public static Authentication assertIsLoggedIn(HttpServletRequest httpServletRequest, String restService, boolean logAssertionFailure) {

        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        try {
            return new Authentication(httpServletRequest);
        } catch (UnauthorizedException e) {
            if (logAssertionFailure) authLogger.warn("Not logged in when calling REST service [" + restService + "]");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    private static void logoutIfLoggedIn(HttpServletRequest httpServletRequest) {

        AuthState authState = new AuthState(httpServletRequest);

        if (authState.isLoggedIn()) {
            authState.logOut();
            logger.info("Found valid session when attempting login. Session invalidated.");
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
            throw new UnauthorizedException("access key [" + accessKey + "] Login rejected. Unrecognized access key.");
        }
    }

    private static UserHttpSessionAttribute createUserSessionAttributeForRole(UserLoginCompositeVO userLoginCompositeVO) throws InfrastructureException {

        try {

            if (userLoginCompositeVO.isInRolePatient()) {
                return new PatientLoginHttpSessionAttribute(userLoginCompositeVO);
            } else if (userLoginCompositeVO.isInRoleTherapist()) {
                return new TherapistHttpSessionAttribute(userLoginCompositeVO);
            } else if (userLoginCompositeVO.isInRoleAdmin()) {
                return new AdminHttpSessionAttribute(userLoginCompositeVO);
            } else {
                throw new IllegalStateException("[" + userLoginCompositeVO.getUsername() + "] Unknown role.");

            }
        } catch (DataSourceException e) {
            throw new InfrastructureException(e);
        }
    }

    private static UserHttpSessionAttribute createUserSessionAttributeForAccessKeyUser(UserAccessKeyCompositeVO userAccessKeyCompositeVO) throws InfrastructureException {
        try {
            return new PatientAnonymousHttpSessionAttribute(userAccessKeyCompositeVO);
        } catch (DataSourceException e) {
            throw new InfrastructureException(e);
        }
    }

    private static void checkPasswordHash(UserLoginCompositeVO userLoginCompositeVO, char[] password) throws UnauthorizedException {

        Argon2 argon2 = Argon2Factory.create();

        try {
            if (!argon2.verify(userLoginCompositeVO.getPasswordHash(), password)) {
                throw new UnauthorizedException("[" + userLoginCompositeVO.getUsername() + "] login rejected. Wrong password.");
            }
        } finally {
            argon2.wipeArray(password);
        }
    }

    private static void createSessionWithUserSessionAttribute(HttpServletRequest httpServletRequest, UserHttpSessionAttribute userHttpSessionAttribute) {
        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(UserHttpSessionAttribute.USER, userHttpSessionAttribute);
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
