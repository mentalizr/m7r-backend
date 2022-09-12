package org.mentalizr.backend.security.auth;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;
import org.mentalizr.backend.Const;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class IntermediateAuthorizationService {

    private static final Logger authLogger = Const.authLogger;

    public static String assertIsIntermediate(HttpServletRequest httpServletRequest) {
        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        try {
            IntermediateAuthentication intermediateAuthentication
                    = new IntermediateAuthentication(httpServletRequest);
            return intermediateAuthentication.getNextRequirement();
        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

}
