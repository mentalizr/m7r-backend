package org.mentalizr.backend.front;

import org.mentalizr.backend.htmlChunks.HtmlChunk;
import org.mentalizr.backend.htmlChunks.HtmlChunkCache;
import org.mentalizr.backend.htmlChunks.InitChunkModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServletLogin extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(FrontControllerServletLogin.class);

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        logger.debug("FrontController (Login) called.");

        httpServletResponse.setContentType("text/html");

        HtmlChunkCache htmlChunkCache = new HtmlChunkCache(httpServletRequest.getServletContext());
        String initChunk = htmlChunkCache.getChunkAsString(HtmlChunk.INIT);

//        logger.debug("chunk as template:\n" + initChunk);

        InitChunkModifier initChunkModifier = new InitChunkModifier(initChunk);
        String initChunkWithLoginEntry = initChunkModifier.withEntry(HtmlChunk.LOGIN);

//        logger.debug("chunk with login Entry:\n" + initChunkWithLoginEntry);

        httpServletResponse.getWriter().println(initChunkWithLoginEntry);

//        logger.debug("doGet finished");
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.debug("doPost(...) called. Ignore silently.");
    }

}
