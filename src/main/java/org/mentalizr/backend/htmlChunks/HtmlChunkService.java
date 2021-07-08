package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.auth.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

public class HtmlChunkService {

    public static InputStream getHtmlChunk(HtmlChunk htmlChunk, HttpServletRequest httpServletRequest) {

        HtmlChunkManager htmlChunkManager = new HtmlChunkManager(httpServletRequest);

        try {
            return htmlChunkManager.getHtmlChunk(htmlChunk);
        } catch (UnauthorizedException e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } catch (IOException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
