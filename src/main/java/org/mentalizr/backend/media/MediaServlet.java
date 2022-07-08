package org.mentalizr.backend.media;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.BrandingConfiguration;
import org.mentalizr.backend.config.DefaultLoginScreen;
import org.mentalizr.backend.front.FrontControllerServlet;
import org.mentalizr.backend.htmlChunks.*;
import org.mentalizr.backend.utils.IOUtils;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MediaServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MediaServlet.class);

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        logger.debug("MediaServlet called.");

        httpServletResponse.setContentType("video/mp4");

        ContentManager contentManager = ApplicationContext.getContentManager();
        Path mediaPath;
        try {
            mediaPath = contentManager.getMediaResource("sport", "test.txt");
        } catch (ContentManagerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        OutputStream outputStream = httpServletResponse.getOutputStream();
        InputStream inputStream = new FileInputStream(mediaPath.toFile());
        IOUtils.pump(inputStream, outputStream);



//        httpServletResponse.setContentType("text/html");
//
//        HtmlChunkRegistry htmlChunkRegistry = new HtmlChunkRegistry(httpServletRequest.getServletContext());
//        String initChunk = htmlChunkRegistry.getChunk(HtmlChunkInit.NAME).asString();
//
//        BrandingConfiguration brandingConfiguration = ApplicationContext.getBrandingConfiguration();
//        ApplicationConfigGenericSO applicationConfigGenericSO = brandingConfiguration.getApplicationConfigGenericSO();
//        DefaultLoginScreen defaultLoginScreen = new DefaultLoginScreen(applicationConfigGenericSO.getDefaultLoginScreen());
//        HtmlChunkModifierInit htmlChunkModifierInit = new HtmlChunkModifierInit(initChunk);
//        if (defaultLoginScreen.isAccessKey()) {
//            htmlChunkModifierInit.addEntry(HtmlChunkLoginVoucher.NAME);
//        } else {
//            htmlChunkModifierInit.addEntry(HtmlChunkLogin.NAME);
//        }
//
//        httpServletResponse.getWriter().println(htmlChunkModifierInit.getModifiedChunk());
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.debug("doPost(...) called. Ignore silently.");
    }


}
