package org.mentalizr.backend.security.session;

import org.mentalizr.backend.security.session.attributes.staging.StagingAttribute;
import org.mentalizr.backend.security.session.attributes.staging.StagingValid;
import org.mentalizr.backend.security.session.attributes.user.*;
import org.mentalizr.backend.security.session.user.*;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static final Logger authLogger = LoggerFactory.getLogger("m7r-auth");

    public static void createSessionForLogin(HttpServletRequest httpServletRequest, UserLoginCompositeVO userLoginCompositeVO) throws InfrastructureException {
        UserHttpSessionAttribute userHttpSessionAttribute = createUserSessionAttributeForRole(userLoginCompositeVO);
        StagingAttribute stagingAttribute = createStagingAttribute(userLoginCompositeVO);

        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(UserHttpSessionAttribute.USER, userHttpSessionAttribute);
        httpSession.setAttribute(StagingAttribute.ATTRIBUTE_NAME, stagingAttribute);
    }

    public static void createSessionForAccessKey(HttpServletRequest httpServletRequest, UserAccessKeyCompositeVO userAccessKeyCompositeVO) throws InfrastructureException {
        UserHttpSessionAttribute userHttpSessionAttribute = createUserSessionAttributeForAccessKeyUser(userAccessKeyCompositeVO);
        StagingAttribute stagingAttribute = createStagingAttribute(userAccessKeyCompositeVO);

        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(UserHttpSessionAttribute.USER, userHttpSessionAttribute);
        httpSession.setAttribute(StagingAttribute.ATTRIBUTE_NAME, stagingAttribute);
    }

    public static boolean hasSessionInAnyStaging(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        return httpSession != null;
    }

    public static void invalidate(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) httpSession.invalidate();
    }

    public static void invalidatePreexisting(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) {
            UserHttpSessionAttribute userHttpSessionAttribute = (UserHttpSessionAttribute) httpSession.getAttribute(UserHttpSessionAttribute.USER);
            httpSession.invalidate();

            if (userHttpSessionAttribute != null) {
                authLogger.info("[" + userHttpSessionAttribute.getUserVO().getId() + "] logout");
            } else {
                logger.error("Session inconsistent: session found valid without session attribute. Unknown user logged out.");
            }
        }
    }

    private static StagingAttribute createStagingAttribute(UserLoginCompositeVO userLoginCompositeVO) {
        // TODO Create Staging here
        return new StagingValid();
    }

    private static StagingAttribute createStagingAttribute(UserAccessKeyCompositeVO userAccessKeyCompositeVO) {
        // TODO Create Staging here
        return new StagingValid();
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

}
