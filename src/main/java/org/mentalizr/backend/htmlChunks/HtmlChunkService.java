package org.mentalizr.backend.htmlChunks;

import de.arthurpicht.webAccessControl.auth.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

public class HtmlChunkService {

    public static InputStream getHtmlChunk(String chunkName, HttpServletRequest httpServletRequest) {
        try {
            HtmlChunkManager htmlChunkManager = new HtmlChunkManager(httpServletRequest);
            return htmlChunkManager.getHtmlChunk(chunkName);
        } catch (UnauthorizedException e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } catch (IOException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
