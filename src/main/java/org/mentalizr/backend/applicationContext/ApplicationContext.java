package org.mentalizr.backend.applicationContext;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.backend.accessControl.WACContextInitializer;
import org.mentalizr.backend.config.infraUser.InfraUserConfiguration;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.config.instance.InstanceConfigurationFactory;
import org.mentalizr.backend.htmlChunks.HtmlChunkCache;
import org.mentalizr.backend.htmlChunks.reader.ProductionHtmlChunkReader;
import org.mentalizr.commons.paths.container.TomcatContainerContentDir;
import org.mentalizr.commons.paths.host.hostDir.M7rHostPolicyDir;
import org.mentalizr.commons.paths.host.hostDir.M7rInfraUserConfigFile;
import org.mentalizr.commons.paths.host.hostDir.M7rInstanceConfigFile;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.mongo.MongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.nio.file.Path;

public class ApplicationContext {

    // TODO Include EventProcessor here?

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
    private static InstanceConfiguration instanceConfiguration;
    private static InfraUserConfiguration infraUserConfiguration;
    private static ContentManager contentManager;
    private static PolicyCache policyCache;
    private static HtmlChunkCache htmlChunkCache;
    private static boolean isInitialized = false;

    public static void initialize(ServletContext servletContext) {
        logger.info("Start ApplicationContext initialization.");

        try {
            WACContextInitializer.init();
            instanceConfiguration = loadInstanceConfiguration();
            infraUserConfiguration = loadInfraUserConfiguration();
            MongoDB.initialize(infraUserConfiguration);
            policyCache = PolicyCache.createInstance(instanceConfiguration);
            htmlChunkCache = new HtmlChunkCache(
                    new ProductionHtmlChunkReader(servletContext, policyCache),
                    instanceConfiguration.getApplicationConfigGenericSO());
            contentManager = initContentManager();
        } catch (InitializationException e) {
            logger.error("Initialization failed. " + e.getMessage());
            throw new InitializationException(e.getMessage(), e);
        } catch (RuntimeException e) {
            logger.error("Initialization failed. " + e.getMessage(), e);
            throw new InitializationException(e.getMessage(), e);
        }

        isInitialized = true;
        logger.info("ApplicationContext initialized successfully.");
    }

    private static InfraUserConfiguration loadInfraUserConfiguration() {
        M7rInfraUserConfigFile m7rInfraUserConfigFile = M7rInfraUserConfigFile.createInstance();
        Path path = m7rInfraUserConfigFile.asPath();
        if (!FileUtils.isExistingRegularFile(path)) throw new InitializationException(
                "Config file [" + M7rInfraUserConfigFile.NAME + "] not found. [" + path.toAbsolutePath() + "].");
        return new InfraUserConfiguration(m7rInfraUserConfigFile);
    }

    private static InstanceConfiguration loadInstanceConfiguration() {
        Path path = M7rInstanceConfigFile.createInstance().asPath();
        if (!FileUtils.isExistingRegularFile(path)) throw new InitializationException(
                "Config file [" + M7rInstanceConfigFile.NAME + "] not found. [" + path.toAbsolutePath() + "].");
        return InstanceConfigurationFactory.createProjectConfigurationByPath(path);
    }

    private static ContentManager initContentManager() {
        Path contentRoot = new TomcatContainerContentDir().asPath();
        try {
            return ContentManager.getInstanceForContentRoot(contentRoot);
        } catch (ContentManagerException e) {
            throw new InitializationException("Initialization of ContentManager failed. Cause: " + e.getMessage(), e);
        }
    }

    public static InstanceConfiguration getInstanceConfiguration() {
        assertIsInitialized();
        return instanceConfiguration;
    }

    public static InfraUserConfiguration getInfraUserConfiguration() {
        assertIsInitialized();
        return infraUserConfiguration;
    }

    public static ContentManager getContentManager() {
        assertIsInitialized();
        return contentManager;
    }

    public static PolicyCache getPolicyCache() {
        assertIsInitialized();
        return policyCache;
    }

    public static HtmlChunkCache getHtmlChunkCache() {
        assertIsInitialized();
        return htmlChunkCache;
    }

    private static void assertIsInitialized() {
        if (!isInitialized) throw new IllegalStateException("ApplicationContext not yet initialized.");
    }

}
