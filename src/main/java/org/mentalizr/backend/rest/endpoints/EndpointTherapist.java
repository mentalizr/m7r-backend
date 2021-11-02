package org.mentalizr.backend.rest.endpoints;

import org.bson.Document;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.TherapistHttpSessionAttribute;
import org.mentalizr.backend.config.ProjectConfiguration;
import org.mentalizr.backend.mock.PatientMessagesSOMock;
import org.mentalizr.backend.mock.PatientsOverviewSOMock;
import org.mentalizr.backend.patientMessagesSOCreator.PatientMessagesSOCreator;
import org.mentalizr.backend.patientsOverviewSOCreator.PatientsOverviewSOCreator;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.backend.rest.serviceWorkload.common.CommonServiceWorkload;
import org.mentalizr.commons.Dates;
import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackData;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataConverter;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.persistence.mongo.formData.FormDataTimestampUpdater;
import org.mentalizr.serviceObjects.frontend.patient.formData.FeedbackSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.therapist.ApplicationConfigTherapistSO;
import org.mentalizr.serviceObjects.frontend.therapist.PatientsOverviewSO;
import org.mentalizr.serviceObjects.frontend.therapist.feedbackSubmission.FeedbackSubmissionSO;
import org.mentalizr.serviceObjects.frontend.therapist.patientMessage.PatientMessagesSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

        TherapistHttpSessionAttribute therapistHttpSessionAttribute
                = AuthorizationService.assertIsLoggedInAsTherapist(httpServletRequest);

        // TODO debug
        logger.debug("Therapist username: [" + therapistHttpSessionAttribute.getUserLoginVO().getUsername() + "].");

        // TODO remove mock
        //noinspection SpellCheckingInspection
        if (therapistHttpSessionAttribute.getUserLoginVO().getUsername().equals("tmock")) {
            logger.debug("Create PatientsOverviewSOMock...");
            PatientsOverviewSO patientsOverviewSO = PatientsOverviewSOMock.createPatientsOverviewSO();
            logger.debug("PatientsOverviewSOMock created.");
            return patientsOverviewSO;
        }

        PatientsOverviewSOCreator patientsOverviewSOCreator
                = new PatientsOverviewSOCreator(therapistHttpSessionAttribute.getRoleTherapistVO());
        return patientsOverviewSOCreator.create();
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

        // TODO remove mock
        //noinspection SpellCheckingInspection
        if (therapistHttpSessionAttribute.getUserLoginVO().getUsername().equals("tmock")) {
            PatientMessagesSO patientMessagesSO = PatientMessagesSOMock.createPatientMessagesSO(therapistId, patientId);
            logger.debug("PatientMessagesSO created as mock.");
            return patientMessagesSO;
        }

        PatientMessagesSOCreator patientMessagesSOCreator = new PatientMessagesSOCreator(patientId, therapistId);
        PatientMessagesSO patientMessagesSO = patientMessagesSOCreator.create();
        return patientMessagesSO;
    }

    @POST
    @Path("therapist/submitFeedback")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitFeedback(FeedbackSubmissionSO feedbackSubmissionSO,
                                 @Context HttpServletRequest httpServletRequest) {

        logger.debug("[therapist/submitFeedback]");

        TherapistHttpSessionAttribute therapistHttpSessionAttribute
                = AuthorizationService.assertIsLoggedInAsTherapist(httpServletRequest);
        String therapistId = therapistHttpSessionAttribute.getUserVO().getId();
        String userId = feedbackSubmissionSO.getUserId();
        String contentId = feedbackSubmissionSO.getContentId();

        FormDataSO formDataSO;
        try {
            formDataSO = FormDataDAO.fetch(userId, contentId);
        } catch (DocumentNotFoundException e) {
            throw new WebApplicationException("Inconsistency check failed: No FormData found for submitted feedback.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        if (!FormDataSOs.isExercise(formDataSO))
            throw new WebApplicationException("Inconsistency check failed: Content is not exercise.",
                    Response.Status.INTERNAL_SERVER_ERROR);

        if (FormDataSOs.hasFeedback(formDataSO))
            throw new WebApplicationException("Inconsistency check failed: Feedback already submitted.",
                    Response.Status.INTERNAL_SERVER_ERROR);

        FeedbackSO feedbackSO = new FeedbackSO();
        feedbackSO.setText(feedbackSubmissionSO.getFeedback());
        feedbackSO.setCreatedTimestamp(Dates.currentTimestampAsISO());
        feedbackSO.setTherapistId(therapistId);
        feedbackSO.setSeenByPatient(false);
        feedbackSO.setSeenByPatientTimestamp(Dates.epochAsISO());
        formDataSO.setFeedback(feedbackSO);

        FormDataDAO.createOrUpdate(formDataSO);

        return ResponseFactory.ok();
    }

    @GET
    @Path("therapist/programContent/{contentId}")
    @Produces(MediaType.TEXT_HTML)
    public Response programContent(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[therapist/programContent] [" + contentId + "]");

        assertIsLoggedInAsTherapist(httpServletRequest);

        return CommonServiceWorkload.getProgramContent(contentId);
    }


}
