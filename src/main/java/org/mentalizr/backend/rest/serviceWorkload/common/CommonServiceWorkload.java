package org.mentalizr.backend.rest.serviceWorkload.common;

import org.bson.Document;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ContentNotFoundException;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackData;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataConverter;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataMongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CommonServiceWorkload {

    private static final Logger logger = LoggerFactory.getLogger(CommonServiceWorkload.class);

    public static Response getProgramContent(String contentId) {
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            java.nio.file.Path stepContentFile = contentManager.getContent(contentId);
            FileInputStream fileInputStream = new FileInputStream(stepContentFile.toFile());
            return Response.ok(fileInputStream).build();
        } catch (ContentNotFoundException | FileNotFoundException e) {
            logger.error("Content not found. Cause: " + e.getMessage());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

}
