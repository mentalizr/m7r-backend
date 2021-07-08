package org.mentalizr.backend.servletContext;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.Configuration;
import org.mentalizr.backend.proc.eventProcessor.EventProcessor;
import org.mentalizr.contentManager.ContentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.nio.file.Path;

/**
 * This ServletContextListener is configured in web.xml.
 */
public class ApplicationContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("ServletContext initialized: initialize ApplicationContext");
        ApplicationContext.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

}
