package org.mentalizr.backend.security.session;

import org.mentalizr.backend.Const;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.security.session.attributes.SecurityAttribute;
import org.mentalizr.backend.security.session.attributes.staging.StagingAttribute;
import org.mentalizr.backend.security.session.attributes.staging.StagingIntermediate;
import org.mentalizr.backend.security.session.attributes.staging.StagingValid;
import org.mentalizr.backend.security.session.attributes.staging.requirements.Requirements;
import org.mentalizr.backend.security.session.attributes.staging.requirements.RequirementsFactory;
import org.mentalizr.backend.security.session.attributes.user.*;
import org.mentalizr.backend.utils.HttpSessions;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static final Logger authLogger = Const.authLogger;

    public static String createSessionForLogin(HttpServletRequest httpServletRequest, UserLoginCompositeVO userLoginCompositeVO) throws InfrastructureException {
        StagingAttribute stagingAttribute = createStagingAttribute(userLoginCompositeVO);
        UserHttpSessionAttribute userHttpSessionAttribute = createUserSessionAttributeForRole(userLoginCompositeVO);
        SecurityAttribute securityAttribute = new SecurityAttribute(stagingAttribute, userHttpSessionAttribute);

        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(SecurityAttribute.NAME, securityAttribute);

        return stagingAttribute.toString();
    }

    public static String createSessionForAccessKey(HttpServletRequest httpServletRequest, UserAccessKeyCompositeVO userAccessKeyCompositeVO) throws InfrastructureException {
        UserHttpSessionAttribute userHttpSessionAttribute = createUserSessionAttributeForAccessKeyUser(userAccessKeyCompositeVO);
        StagingAttribute stagingAttribute = createStagingAttribute(userAccessKeyCompositeVO);
        SecurityAttribute securityAttribute = new SecurityAttribute(stagingAttribute, userHttpSessionAttribute);

        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(SecurityAttribute.NAME, securityAttribute);

        return stagingAttribute.toString();
    }

    public static boolean hasSessionInAnyStaging(HttpServletRequest httpServletRequest) {
        return HttpSessions.hasRunningSession(httpServletRequest);
    }

    public static boolean hasValidSession(HttpServletRequest httpServletRequest) {
        if (!HttpSessions.hasRunningSession(httpServletRequest)) return false;
        SecurityAttribute securityAttribute = getSecurityAttribute(httpServletRequest);
        return securityAttribute.isValid();
    }

    public static boolean hasIntermediateSession(HttpServletRequest httpServletRequest) {
        if (!HttpSessions.hasRunningSession(httpServletRequest)) return false;
        SecurityAttribute securityAttribute = getSecurityAttribute(httpServletRequest);
        return securityAttribute.isIntermediate();
    }

    public static void invalidate(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) httpSession.invalidate();
    }

    public static void invalidatePreexisting(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) {
            SecurityAttribute securityAttribute = getSecurityAttribute(httpServletRequest);
            UserHttpSessionAttribute userHttpSessionAttribute = securityAttribute.getUserHttpSessionAttribute();
            httpSession.invalidate();
            authLogger.info("[" + userHttpSessionAttribute.getUserVO().getId() + "] logout");
        }
    }

    public static SecurityAttribute getSecurityAttribute(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = HttpSessions.getRunningSession(httpServletRequest);
        SecurityAttribute securityAttribute = (SecurityAttribute) httpSession.getAttribute(SecurityAttribute.NAME);
        if (securityAttribute == null)
            throw new IllegalStateException("No SecurityAttribute found in running session.");
        return securityAttribute;
    }

    public static String getSessionId(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = HttpSessions.getRunningSession(httpServletRequest);
        return httpSession.getId();
    }

    private static StagingAttribute createStagingAttribute(UserLoginCompositeVO userLoginCompositeVO) {
        Requirements requirements = RequirementsFactory.createRequirements(userLoginCompositeVO);
        if (requirements.hasRequirements()) {
            return new StagingIntermediate(requirements);
        } else {
            return new StagingValid();
        }
    }

    private static StagingAttribute createStagingAttribute(UserAccessKeyCompositeVO userAccessKeyCompositeVO) {
        Requirements requirements = RequirementsFactory.createRequirements(userAccessKeyCompositeVO);
        if (requirements.hasRequirements()) {
            return new StagingIntermediate(requirements);
        } else {
            return new StagingValid();
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
                throw new IllegalStateException("[" + userLoginCompositeVO.getUserLoginVO().getUsername() + "] Unknown role.");
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
