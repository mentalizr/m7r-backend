package org.mentalizr.backend.servletContext;

import org.mentalizr.backend.proc.eventProcessor.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This ServletContextListener is configured in web.xml.
 */
public class EventProcessorContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessorContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("ServletContext initialized: start EventProcessor.");
        EventProcessor.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("ServletContext destroyed: send stop signal to EventProcessor.");
        EventProcessor.stop();
    }

}
