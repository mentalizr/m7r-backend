package org.mentalizr.backend.rest.endpoints;

import de.arthurpicht.utils.io.InputStreams;
import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.backend.rest.entities.UserFactory;
import org.mentalizr.commons.paths.container.TomcatContainerImgBaseTmpDir;
import org.mentalizr.serviceObjects.frontend.application.UserSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

@Path("v1")
public class Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(Endpoint.class);

    @GET
    public String defaultMessage() {
        String message = "mentalizr REST interface is up and running. ";
        InputStream inputStream = InputStreams.getFileFromResourceAsStream("builddate.txt");
        try {
            List<String> strings = InputStreams.toStrings(inputStream);
            message += "Build time: " + strings.get(0);
        } catch (IOException | RuntimeException e) {
            message += "Could not read builddate.txt";
        }

        return message + "\n";
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "mentalizr REST interface is up and running.";
    }

//    @GET
//    @Path("reloadcontent")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String reloadcontent() throws IOException {
//        logger.debug("[reloadcontent]");
//        ContentManager contentManager = ApplicationContext.getContentManager();
//        try {
//            contentManager.reload();
//        } catch (ContentManagerException e) {
//            logger.error("Reloading content failed. Cause: " + e.getMessage());
//            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
//        }
//        return "Content reloaded!";
//    }

    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSO user(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[user]");

        try {
            Authorization authorization = AccessControl.assertValidSessionForAnyRole(httpServletRequest);
            M7rAuthorization m7rAuthorization = new M7rAuthorization(authorization);
            M7rUser m7rUser = m7rAuthorization.getUserAsM7rUser();
            return UserFactory.getInstance(m7rUser);
        } catch (UnauthorizedException e) {
            logger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("userImgThumbnail")
    @Produces("image/png")
    public Response userImgThumbnail(
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[userImgThumbnail]");

        try {
            AccessControl.assertValidSessionForAnyRole(httpServletRequest);
        } catch (UnauthorizedException e) {
            logger.warn(e.getMessage());
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        File imageFile = new File(new TomcatContainerImgBaseTmpDir().toAbsolutePathString(), "dummies/DummyAvatar.png");
        try {
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            return Response.ok(fileInputStream).build();
        } catch (FileNotFoundException e) {
            logger.error("[patientImgThumbnail] Image file not found: " + imageFile.getAbsolutePath());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
