package org.mentalizr.backend.rest.endpoints;

import org.bson.Document;
import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackData;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataConverter;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataMongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("v1")
public class EndpointTherapist {

    private static final Logger logger = LoggerFactory.getLogger(EndpointTherapist.class);
    private static final Logger authLogger = LoggerFactory.getLogger("m7r-auth");

    @POST
    @Path("sendFeedbackData")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendFeedbackData(FeedbackData feedbackData, @Context HttpServletRequest httpServletRequest) {
        logger.debug("[sendFeedbackData]");

        AuthorizationService.assertIsLoggedInAsTherapist(httpServletRequest);

        Document document = FeedbackDataConverter.convert(feedbackData);
        FeedbackDataMongoHandler.createOrUpdate(document);

        logger.debug(document.toJson());

        // TODO Event Feedback Submitted Event
    }

}
