package org.mentalizr.backend.rest.endpoints;

import org.bson.Document;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.PatientHttpSessionAttribute;
import org.mentalizr.backend.auth.TherapistHttpSessionAttribute;
import org.mentalizr.backend.config.ProjectConfiguration;
import org.mentalizr.backend.mock.PatientMessagesSOMock;
import org.mentalizr.backend.mock.PatientsOverviewSOMock;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackData;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataConverter;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataMongoHandler;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.therapist.ApplicationConfigTherapistSO;
import org.mentalizr.serviceObjects.frontend.therapist.PatientsOverviewSO;
import org.mentalizr.serviceObjects.frontend.therapist.patientMessage.PatientMessagesSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatient;
import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsTherapist;

@Path("v1")
public class EndpointTherapist {

    private static final Logger logger = LoggerFactory.getLogger(EndpointTherapist.class);
    private static final Logger authLogger = LoggerFactory.getLogger("m7r-auth");

    @GET
    @Path("therapist/appConfig")
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationConfigTherapistSO appConfig(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[therapist:appConfig]");

        assertIsLoggedInAsTherapist(httpServletRequest);

        ProjectConfiguration projectConfiguration = ApplicationContext.getProjectConfiguration();
        // TODO: returns projectConfiguration for default program -> make generic
        return projectConfiguration.getApplicationConfigTherapistSO("some-program");
    }

    @POST
    @Path("sendFeedbackData")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendFeedbackData(FeedbackData feedbackData, @Context HttpServletRequest httpServletRequest) {
        logger.debug("[therapist:sendFeedbackData]");

        AuthorizationService.assertIsLoggedInAsTherapist(httpServletRequest);

        Document document = FeedbackDataConverter.convert(feedbackData);
        FeedbackDataMongoHandler.createOrUpdate(document);

        logger.debug(document.toJson());

        // TODO Event Feedback Submitted Event
    }

    @GET
    @Path("therapist/patientsOverview")
    @Produces(MediaType.APPLICATION_JSON)
    public PatientsOverviewSO getPatientsOverview(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[therapist:getPatientsOverview]");

        AuthorizationService.assertIsLoggedInAsTherapist(httpServletRequest);

        logger.debug("Create PatientsOverviewSOMock...");

        // TODO mocked
        PatientsOverviewSO patientsOverviewSO = PatientsOverviewSOMock.createPatientsOverviewSO();

        logger.debug("PatientsOverviewSOMock created.");

        return patientsOverviewSO;
    }

    @GET
    @Path("therapist/patientMessages/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public PatientMessagesSO getPatientMessages(
            @PathParam("patientId") String patientId,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[therapist:getPatientMessages]");

        TherapistHttpSessionAttribute therapistHttpSessionAttribute
                = AuthorizationService.assertIsLoggedInAsTherapist(httpServletRequest);
        String therapistId = therapistHttpSessionAttribute.getUserVO().getId();

        // TODO mocked
        PatientMessagesSO patientMessagesSO = PatientMessagesSOMock.createPatientMessagesSO(therapistId, patientId);

        logger.debug("PatientMessagesSO created.");

        return patientMessagesSO;
    }

}
