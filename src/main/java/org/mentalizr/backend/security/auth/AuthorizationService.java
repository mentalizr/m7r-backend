package org.mentalizr.backend.security.auth;

import org.mentalizr.backend.Const;
import org.mentalizr.backend.security.session.attributes.SecurityAttribute;
import org.mentalizr.backend.security.session.attributes.user.*;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AuthorizationService {

    private static final Logger authLogger = Const.authLogger;

    public static UserHttpSessionAttribute assertIsLoggedIn(HttpServletRequest httpServletRequest) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        try {
            return checkAsUser(httpServletRequest);
        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    public static SecurityAttribute assertIsLoggedInAsAdmin(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        return checkAsAdmin(httpServletRequest);
    }

    public static PatientHttpSessionAttribute assertIsLoggedInAsPatient(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        return checkAsPatient(httpServletRequest);
    }

    public static PatientHttpSessionAttribute assertIsLoggedInAsPatientWithUserId(HttpServletRequest httpServletRequest, String actualUserId) throws UnauthorizedException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        PatientHttpSessionAttribute patientHttpSessionAttribute = checkAsPatient(httpServletRequest);

        UserVO userVO = patientHttpSessionAttribute.getUserVO();
        if (!userVO.getId().equals(actualUserId)) {
            throw new UnauthorizedException("[checkAsPatientWithUserId] failed: Expected UserId: " + userVO.getId() + " Actual UserId: " +  actualUserId);
        }

        return patientHttpSessionAttribute;
    }

    public static TherapistHttpSessionAttribute assertIsLoggedInAsTherapist(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        return checkAsTherapist(httpServletRequest);
    }

    public static UserHttpSessionAttribute assertIsLoggedInAsLoginUser(HttpServletRequest httpServletRequest) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        try {
            UserHttpSessionAttribute userHttpSessionAttribute = checkAsUser(httpServletRequest);
            if (userHttpSessionAttribute instanceof PatientAnonymousHttpSessionAttribute)
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            return userHttpSessionAttribute;
        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    private static SecurityAttribute checkAsUser(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        Authorization authorization = new Authorization(httpServletRequest);
        return authorization.getSecurityAttribute();
    }

    private static SecurityAttribute checkAsPatient(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        Authorization authorization = new Authorization(httpServletRequest);
        if (!authorization.isPatient())
            throw new UnauthorizedException("User not in role PATIENT.");
        return authorization.getSecurityAttribute();
    }

    private static SecurityAttribute checkAsAdmin(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        Authorization authorization = new Authorization(httpServletRequest);
        if (!authorization.isAdmin())
            throw new UnauthorizedException("User not in role ADMIN.");
        return authorization.getSecurityAttribute();
    }

    private static SecurityAttribute checkAsTherapist(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        Authorization authorization = new Authorization(httpServletRequest);
        if (!authorization.isTherapist())
            throw new UnauthorizedException("User not in role THERAPIST.");
        return authorization.getSecurityAttribute();
    }

}
