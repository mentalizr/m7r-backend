package org.mentalizr.backend.front;

import org.mentalizr.backend.config.Configuration;
import org.mentalizr.backend.htmlChunks.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(FrontControllerServlet.class);

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        logger.debug("FrontController called.");

        httpServletResponse.setContentType("text/html");

        HtmlChunkRegistry htmlChunkRegistry = new HtmlChunkRegistry(httpServletRequest.getServletContext());
        String initChunk = htmlChunkRegistry.getChunk(HtmlChunkInit.NAME).asString();

        HtmlChunkModifierInit htmlChunkModifierInit = new HtmlChunkModifierInit(initChunk);
        if (Configuration.getDefaultLogin() == Configuration.DefaultLogin.ACCESS_KEY) {
            htmlChunkModifierInit.addEntry(HtmlChunkLoginVoucher.NAME);
        } else {
            htmlChunkModifierInit.addEntry(HtmlChunkLogin.NAME);
        }

        httpServletResponse.getWriter().println(htmlChunkModifierInit.getModifiedChunk());
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.debug("doPost(...) called. Ignore silently.");
    }

}
