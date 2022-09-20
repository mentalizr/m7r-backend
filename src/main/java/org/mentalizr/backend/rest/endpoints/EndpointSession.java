package org.mentalizr.backend.rest.endpoints;

import de.arthurpicht.webAccessControl.auth.*;
import org.mentalizr.backend.Const;
import org.mentalizr.backend.rest.entities.factories.SessionStatusFactory;
import org.mentalizr.backend.utils.HttpSessions;
import org.mentalizr.serviceObjects.SessionStatusSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class EndpointSession {

    private static final Logger logger = LoggerFactory.getLogger(EndpointSession.class);
    private static final Logger authLogger = Const.authLogger;
    private static final int DELAY_ON_NEXT_CONTENT = 0;

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@FormParam("user") String username,
                        @FormParam("password") String password,
                        @FormParam("rememberMe") boolean rememberMe,
                        @Context HttpServletRequest httpServletRequest) {

        logger.debug("[login] UserLogin: " + username + " rememberMe: " + rememberMe);

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        try {

            // TODO: Prüfen, ob Übergabe des Passwortes an Endpoint als char-Array
            // erfolgen kann. Vergl.:
            // https://www.igorkromin.net/index.php/2017/05/05/posting-array-data-from-a-web-form-to-a-jersey-rest-service/
            char[] passwordCharArray = password.toCharArray();

            AccessControl.login(httpServletRequest, username, passwordCharArray);
            if (rememberMe) HttpSessions.rememberMe(httpServletRequest);

            return "success";

        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new WebApplicationException((Response.Status.INTERNAL_SERVER_ERROR));

        }
    }

    @POST
    @Path("loginAccessKey")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String loginAccessKey(@FormParam("accessKey") String accessKey,
                        @FormParam("rememberMe") boolean rememberMe,
                        @Context HttpServletRequest httpServletRequest) {

        logger.debug("[loginAccessKey] AccessKey: " + accessKey + " rememberMe: " + rememberMe);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

            AccessControl.loginWithAccessKey(httpServletRequest, accessKey);
            if (rememberMe) HttpSessions.rememberMe(httpServletRequest);
            return "success";

        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("logout")
    @Produces(MediaType.TEXT_PLAIN)
    public String logout(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[logout]");
        AccessControl.logout(httpServletRequest);
        return "logout";
    }

    @GET
    @Path("noop")
    @Produces(MediaType.TEXT_PLAIN)
    public String noop(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[noop]");
        try {
            AccessControl.assertHasSessionInAnyStaging(httpServletRequest);
        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        return "noop";
    }

    @GET
    @Path("sessionStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public SessionStatusSO sessionStatus(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[sessionStatus]");

//        if (!SessionManager.hasSessionInAnyStaging(httpServletRequest))
//            return SessionStatusFactory.getInstanceForInvalidSession();

        Authorization authorization;
        try {
            authorization = new Authorization(httpServletRequest);
        } catch (UnauthorizedException e) {
            return SessionStatusFactory.getInstanceForInvalidSession();
        }

        if (authorization.isIntermediate()) {
            return SessionStatusFactory.getInstanceForIntermediateSession(
                    authorization.getRoleName(),
                    authorization.getSessionId(),
                    authorization.getNextRequirement());
        } else if (authorization.isValid()) {
            return SessionStatusFactory.getInstanceForValidSession(
                    authorization.getRoleName(),
                    authorization.getSessionId());
        }

//        if (SessionManager.hasIntermediateSession(httpServletRequest)) {
//            IntermediateAuthentication intermediateAuthentication;
//            try {
//                intermediateAuthentication = new IntermediateAuthentication(httpServletRequest);
//            } catch (UnauthorizedException e) {
//                throw new IllegalStateException("Session expected to be staged as INTERMEDIATE.");
//            }
//            return SessionStatusFactory.getInstanceForIntermediateSession(
//                    intermediateAuthentication.getRoleName(),
//                    intermediateAuthentication.getSessionId(),
//                    intermediateAuthentication.getNextRequirement());
//        }
//
//        if (SessionManager.hasValidSession(httpServletRequest)) {
//            try {
//                Authentication authentication = new Authentication(httpServletRequest);
//                String sessionId = authentication.getSessionId();
//                return SessionStatusFactory.getInstanceForValidSession(authentication.getRoleName(), sessionId);
//            } catch (UnauthorizedException e) {
//                throw new IllegalStateException("Session expected to be staged as VALID.");
//            }
//        }

        throw new IllegalStateException("Inconsistent session state.");
    }

}
