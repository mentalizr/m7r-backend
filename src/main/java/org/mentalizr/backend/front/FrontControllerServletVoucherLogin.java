package org.mentalizr.backend.front;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.htmlChunks.HtmlChunkCache;
import org.mentalizr.backend.htmlChunks.definitions.InitVoucherHtmlChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServletVoucherLogin extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(FrontControllerServletVoucherLogin.class);
    private static final long serialVersionUID = -512436247165237131L;

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        logger.debug("FrontController (Voucher) called.");
        httpServletResponse.setContentType("text/html");
        HtmlChunkCache htmlChunkCache = ApplicationContext.getHtmlChunkCache();
        String initChunkAsString = htmlChunkCache.getChunkAsString(InitVoucherHtmlChunk.NAME);
        httpServletResponse.getWriter().println(initChunkAsString);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.debug("doPost(...) called. Ignore silently.");
    }

}
