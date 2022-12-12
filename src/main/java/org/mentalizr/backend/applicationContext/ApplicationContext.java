package org.mentalizr.backend.applicationContext;

import org.mentalizr.backend.accessControl.WACContextInitializer;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.config.instance.InstanceConfigurationFactory;
import org.mentalizr.commons.paths.container.TomcatContainerContentDir;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ApplicationContext {

    // TODO Rebuild Configuration to flat object, include here
    // TODO Include EventProcessor here?

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
    private static InstanceConfiguration instanceConfiguration;
    private static ContentManager contentManager;
    private static boolean isInitialized = false;

    public static void initialize() {
        logger.info("Start ApplicationContext initialization.");

        try {
            WACContextInitializer.init();
            instanceConfiguration = InstanceConfigurationFactory.createProjectConfigurationFromClasspath();
            contentManager = initContentManager();
        } catch (RuntimeException e) {
            logger.error("Initialization failed: " + e.getMessage());
            throw e;
        }

        isInitialized = true;
        logger.info("ApplicationContext initialized successfully.");
    }

    private static ContentManager initContentManager() {
        Path contentRoot = new TomcatContainerContentDir().asPath();
        try {
            return ContentManager.getInstanceForContentRoot(contentRoot);
        } catch (ContentManagerException e) {
            throw new RuntimeException("Initialization of ContentManager failed. Cause: " + e.getMessage(), e);
        }
    }

    public static InstanceConfiguration getBrandingConfiguration() {
        if (!isInitialized) throw new IllegalStateException("ApplicationContext not yet initialized.");
        return instanceConfiguration;
    }

    public static ContentManager getContentManager() {
        if (!isInitialized) throw new IllegalStateException("ApplicationContext not yet initialized.");
        return contentManager;
    }

}
