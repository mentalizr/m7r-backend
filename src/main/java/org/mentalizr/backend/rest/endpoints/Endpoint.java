package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.config.Configuration;
import org.mentalizr.backend.htmlChunks.HtmlChunkService;
import org.mentalizr.backend.rest.entities.UserFactory;
import org.mentalizr.serviceObjects.frontend.application.UserSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedIn;

@Path("v1")
public class Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(Endpoint.class);

    @Context
    private ServletContext context;

    @GET
    public String defaultMessage() {
        return "mentalizr REST interface is up and running.";}

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
    @Path("htmlChunk/{chunkName}")
    @Produces(MediaType.TEXT_HTML)
    public Response htmlChunk(
            @PathParam("chunkName") String chunkName,
            @Context HttpServletRequest httpServletRequest) {

        logger.debug("[htmlChunk] ...");

        logger.debug("[htmlChunk] requested Chunk is: " + chunkName);

        InputStream inputStream = HtmlChunkService.getHtmlChunk(chunkName, httpServletRequest);
        return Response.ok(inputStream).build();
    }

    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSO user(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[user]");

        UserHttpSessionAttribute userHttpSessionAttribute = assertIsLoggedIn(httpServletRequest);
        return UserFactory.getInstance(userHttpSessionAttribute);
    }

    @GET
    @Path("userImgThumbnail")
    @Produces("image/png")
    public Response userImgThumbnail(
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[userImgThumbnail]");

        assertIsLoggedIn(httpServletRequest);

        File imageFile = new File(Configuration.getDirImageRoot(), "dummies/DummyAvatar.png");
        try {
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            return Response.ok(fileInputStream).build();
        } catch (FileNotFoundException e) {
            logger.error("[patientImgThumbnail] Image file not found: " + imageFile.getAbsolutePath());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }



}
