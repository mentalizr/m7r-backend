package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.htmlChunks.HtmlChunk;
import org.mentalizr.backend.htmlChunks.HtmlChunkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

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

        HtmlChunk htmlChunk = HtmlChunk.getHtmlChunkByName(chunkName.toUpperCase());

        logger.debug("[htmlChunk] requested Chunk is: " + htmlChunk.toString());

        InputStream inputStream = HtmlChunkService.getHtmlChunk(htmlChunk, httpServletRequest);
        return Response.ok(inputStream).build();
    }

}
