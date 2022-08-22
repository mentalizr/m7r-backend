package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.security.auth.Authentication;
import org.mentalizr.backend.security.auth.AuthenticationService;
import org.mentalizr.backend.security.auth.Authorization;
import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.entities.factories.SessionStatusFactory;
import org.mentalizr.backend.utils.HttpSessionHelper;
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
    private static final Logger authLogger = LoggerFactory.getLogger("m7r-auth");
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

            AuthenticationService.login(httpServletRequest, username, passwordCharArray);
            if (rememberMe) HttpSessionHelper.rememberMe(httpServletRequest);

            return "success";

        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        } catch (InfrastructureException | RuntimeException e) {
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

            AuthenticationService.loginWithAccessKey(httpServletRequest, accessKey);
            if (rememberMe) HttpSessionHelper.rememberMe(httpServletRequest);
            return "success";

        } catch (UnauthorizedException e) {
            authLogger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        } catch (InfrastructureException | RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new WebApplicationException((Response.Status.INTERNAL_SERVER_ERROR));
        }
    }

    @GET
    @Path("logout")
    @Produces(MediaType.TEXT_PLAIN)
    public String logout(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[logout]");
        AuthenticationService.logout(httpServletRequest);
        return "logout";
    }

    @GET
    @Path("noop")
    @Produces(MediaType.TEXT_PLAIN)
    public String noop(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[noop]");
        AuthenticationService.assertHasSessionInAnyStaging(httpServletRequest, "noop", false);
        return "noop";
    }

    @GET
    @Path("sessionStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public SessionStatusSO sessionStatus(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[sessionStatus]");

        Authentication authentication;
        try {
            authentication = new Authentication(httpServletRequest);
        } catch (UnauthorizedException e) {
            return SessionStatusFactory.getInstanceForInvalidSession();
        }

        Authorization authorization = new Authorization(authentication);
        String sessionId = authentication.getHttpSessionId();
        return SessionStatusFactory.getInstanceForValidSession(authorization.getUserRole(), sessionId);
    }

}
