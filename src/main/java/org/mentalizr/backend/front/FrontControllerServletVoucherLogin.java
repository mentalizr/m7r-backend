package org.mentalizr.backend.front;

import org.mentalizr.backend.htmlChunks.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontControllerServletVoucherLogin extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(FrontControllerServletVoucherLogin.class);

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        logger.debug("FrontController (Voucher) called.");

        httpServletResponse.setContentType("text/html");

        HtmlChunkRegistry htmlChunkRegistry = new HtmlChunkRegistry(httpServletRequest.getServletContext());
        String initChunk = htmlChunkRegistry.getChunk(HtmlChunkInit.NAME).asString();

        HtmlChunkModifierInit htmlChunkModifierInit = new HtmlChunkModifierInit(initChunk);
        htmlChunkModifierInit.addEntry(HtmlChunkLoginVoucher.NAME);

        FrontControllerServletHelper.addTitle(htmlChunkModifierInit);

        httpServletResponse.getWriter().println(htmlChunkModifierInit.getModifiedChunk());
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.debug("doPost(...) called. Ignore silently.");
    }

}
