package org.mentalizr.backend.auth;

import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AuthorizationService {

    private static final Logger authLogger = LoggerFactory.getLogger("m7r-auth");

    public static PatientHttpSessionAttribute assertIsLoggedInAsPatient(HttpServletRequest httpServletRequest) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        try {
            return checkAsPatient(httpServletRequest);
        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    public static AdminHttpSessionAttribute assertIsLoggedInAsAdmin(HttpServletRequest httpServletRequest) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);
        try {
            return checkAsAdmin(httpServletRequest);
        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    public static PatientHttpSessionAttribute assertIsLoggedInAsPatientWithUserId(HttpServletRequest httpServletRequest, String actualUserId) {

        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        try {
            PatientHttpSessionAttribute patientHttpSessionAttribute = checkAsPatient(httpServletRequest);

            UserVO userVO = patientHttpSessionAttribute.getUserVO();
            if (!userVO.getUserId().equals(actualUserId)) {
                throw new UnauthorizedException("[checkAsPatientWithUserId] failed: Expected UserId: " + userVO.getUserId() + " Actual UserId: " +  actualUserId);
            }

            return patientHttpSessionAttribute;

        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    private static PatientHttpSessionAttribute checkAsPatient(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        Authorization authorization = new Authorization(httpServletRequest);
        if (!authorization.isPatient())
            throw new UnauthorizedException("User not in role PATIENT.");
        return authorization.getPatientHttpSessionAttribute();
    }

    private static AdminHttpSessionAttribute checkAsAdmin(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        Authorization authorization = new Authorization(httpServletRequest);
        if (!authorization.isAdmin())
            throw new UnauthorizedException("User not in role ADMIN.");
        return authorization.getAdminHttpSessionAttribute();
    }

}
