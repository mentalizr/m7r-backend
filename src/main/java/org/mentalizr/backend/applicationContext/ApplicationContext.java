package org.mentalizr.backend.applicationContext;

import org.mentalizr.backend.config.Configuration;
import org.mentalizr.backend.config.ProjectConfiguration;
import org.mentalizr.backend.servletContext.ApplicationContextListener;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ApplicationContext {

    // TODO Rebuild Configuration to flat object, include here
    // TODO Include EventProcessor here?

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
    private static ProjectConfiguration projectConfiguration;
    private static ContentManager contentManager;
    private static boolean isInitialized = false;

    public static void initialize() {
        logger.info("Start ApplicationContext initialization.");

        try {
            projectConfiguration = new ProjectConfiguration();
            contentManager = initContentManager();
        } catch (RuntimeException e) {
            logger.error("Initialization failed: " + e.getMessage());
            throw e;
        }

        isInitialized = true;
        logger.info("ApplicationContext initialized successfully.");
    }

    private static ContentManager initContentManager() {
        Path contentRoot = Configuration.getDirProgramContentRoot();
        try {
            return ContentManager.getInstanceForContentRoot(contentRoot);
        } catch (ContentManagerException e) {
            throw new RuntimeException("Initialization of ContentManager failed. Cause: " + e.getMessage(), e);
        }
    }

    public static ProjectConfiguration getProjectConfiguration() {
        if (!isInitialized) throw new IllegalStateException("ApplicationContext not yet initialized.");
        return projectConfiguration;
    }

    public static ContentManager getContentManager() {
        if (!isInitialized) throw new IllegalStateException("ApplicationContext not yet initialized.");
        return contentManager;
    }

}
