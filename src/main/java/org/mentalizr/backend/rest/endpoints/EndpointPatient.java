package org.mentalizr.backend.rest.endpoints;

import org.bson.Document;
import org.mentalizr.backend.programSOCreator.FormDataFetcher;
import org.mentalizr.backend.programSOCreator.FormDataFetcherMongo;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.PatientHttpSessionAttribute;
import org.mentalizr.backend.config.Configuration;
import org.mentalizr.backend.config.ProjectConfiguration;
import org.mentalizr.backend.proc.event.ExerciseSubmittedEvent;
import org.mentalizr.backend.programSOCreator.ProgramSOCreator;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.backend.rest.entities.UserFactory;
import org.mentalizr.backend.rest.serviceWorkload.common.CommonServiceWorkload;
import org.mentalizr.commons.Dates;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ContentNotFoundException;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ProgramNotFoundException;
import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackData;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataConverter;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormDataConverter;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormDataTimestampUpdater;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.frontend.application.UserSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOX;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatient;

@Path("v1")
public class EndpointPatient {

    private static final Logger logger = LoggerFactory.getLogger(EndpointPatient.class);

    @GET
    @Path("patient/appConfig")
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationConfigPatientSO appConfig(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[patient:appConfig]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);

        ProjectConfiguration projectConfiguration = ApplicationContext.getProjectConfiguration();
        String projectId = patientHttpSessionAttribute.getPatientProgramVO().getProgramId();
        return projectConfiguration.getApplicationConfigPatientSO(projectId);
    }

    @GET
    @Path("patient/therapist")
    @Produces(MediaType.APPLICATION_JSON)
    public UserSO therapist(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[patient:therapist]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);
        return UserFactory.getInstanceForRelatedTherapist(patientHttpSessionAttribute);
    }

    @GET
    @Path("patient/program")
    @Produces(MediaType.APPLICATION_JSON)
    public ProgramSO program(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[patient:program]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);

        PatientProgramVO patientProgramVO = patientHttpSessionAttribute.getPatientProgramVO();
        ProgramStructure programStructure = obtainProgramStructure(patientProgramVO.getProgramId());
        FormDataFetcher formDataFetcher = new FormDataFetcherMongo();
        ProgramSOCreator programSOCreator = new ProgramSOCreator(
                patientProgramVO.getUserId(),
                patientProgramVO.getBlocking(),
                programStructure,
                formDataFetcher
        );
        return programSOCreator.create();
    }

    private ProgramStructure obtainProgramStructure(String programId) {
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            return contentManager.getProgramStructure(programId);
        } catch (ProgramNotFoundException e) {
            logger.error("Program not found. Cause: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("patient/therapeutImgThumbnail")
    @Produces("image/png")
    public Response therapeutThumbnailImage(
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[therapeutImgThumbnail]");

        assertIsLoggedInAsPatient(httpServletRequest);

        File image = new File(Configuration.getDirImageRoot(), "dummies/DummyAvatar.png");
        try {
            FileInputStream fileInputStream = new FileInputStream(image);
            return Response.ok(fileInputStream).build();
        } catch (FileNotFoundException e) {
            logger.error("[therapeutImgThumbnail] Image file not found: " + image.getAbsolutePath());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // 27.11.2019 Anmerkung zu @Produces("*/*"):
    // Bei Anforderung von img sendet FF seit V65 "image/webp, */*", vorher "*/*"
    // Damit alte FF-Versionen (wie aktuell in AK-Citrixumgebung) keinen HTTP-Fehler 406 sehen
    // auf "*/*" zurück gestellt.
    // vergl.: https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation/List_of_default_Accept_values
    @GET
    @Path("mediaImg/{img}")
    @Produces("*/*")     //image/*   //image/jpeg
    public Response mediaImg(
            @PathParam("img") String img,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[mediaImg] [" + img + "]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);

        PatientProgramVO patientProgramVO = patientHttpSessionAttribute.getPatientProgramVO();
        String programId = patientProgramVO.getProgramId();
        return getMediaResource(programId, img);
    }

    @GET
    @Path("mediaAV/{audioVideo}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response mediaVideo(
            @PathParam("audioVideo") String audioVideo,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[mediaVideo] [" + audioVideo + "]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);

        PatientProgramVO patientProgramVO = patientHttpSessionAttribute.getPatientProgramVO();
        String programId = patientProgramVO.getProgramId();
        return getMediaResource(programId, audioVideo);
    }

    private Response getMediaResource(String programId, String mediaResourceName) {
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            java.nio.file.Path mediaPath = contentManager.getMediaResource(programId, mediaResourceName);
            FileInputStream fileInputStream = new FileInputStream(mediaPath.toFile());
            return Response.ok(fileInputStream).build();
        } catch (ContentManagerException | FileNotFoundException e) {
            logger.error(e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("patient/programContent/{contentId}")
    @Produces(MediaType.TEXT_HTML)
    public Response programContent(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[patient/programContent] [" + contentId + "]");

        assertIsLoggedInAsPatient(httpServletRequest);

        return CommonServiceWorkload.getProgramContent(contentId);
    }

//    @GET
//    @Path("programInfoContent/{contentId}")
//    @Produces(MediaType.TEXT_HTML)
//    public Response programInfoContent(
//            @PathParam("contentId") String contentId,
//            @Context HttpServletRequest httpServletRequest) {
//
//        //TODO This service is equal to 'programContent'. Unify to service 'content'.
//
//        logger.debug("[programInfoContent] [" + contentId + "]");
//
//        assertIsLoggedInAsPatient(httpServletRequest);
//
//        return CommonServiceWorkload.getProgramContent(contentId);
//    }

//    private Response getProgramContent(String contentId) {
//        ContentManager contentManager = ApplicationContext.getContentManager();
//        try {
//            java.nio.file.Path stepContentFile = contentManager.getContent(contentId);
//            FileInputStream fileInputStream = new FileInputStream(stepContentFile.toFile());
//            return Response.ok(fileInputStream).build();
//        } catch (ContentNotFoundException | FileNotFoundException e) {
//            logger.error("Content not found. Cause: " + e.getMessage());
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//    }

    @GET
    @Path("formData/{contentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public FormDataSO getFormData(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        logger.debug("[formDataSO] [" + contentId + "]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);
        UserVO userVO = patientHttpSessionAttribute.getUserVO();

        FormDataSO formDataSO = FormDataDAO.obtain(userVO.getId(), contentId);
        FormDataTimestampUpdater.markFeedbackAsSeenByPatient(formDataSO);

        logger.debug(FormDataSOX.toJsonWithFormatting(formDataSO));
        return formDataSO;
    }

    @POST
    @Path("patient/formData/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveFormData(FormDataSO formDataSO,
                             @Context HttpServletRequest httpServletRequest) {

        logger.debug("[saveFormData]");

        logger.debug("FormDataSO: " + formDataSO.toString());

        AuthorizationService.assertIsLoggedInAsPatientWithUserId(httpServletRequest, formDataSO.getUserId());

        FormDataMongoHandler.mergeWithPreexisting(formDataSO);

        return ResponseFactory.ok();
    }

    @POST
    @Path("patient/formData/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendFormData(FormDataSO formDataSO,
                             @Context HttpServletRequest httpServletRequest) {

        logger.debug("[sendFormData]");

        AuthorizationService.assertIsLoggedInAsPatientWithUserId(httpServletRequest, formDataSO.getUserId());

        // TODO debug
        logger.debug("FormDate to save:");
        logger.debug(FormDataSOX.toJsonWithFormatting(formDataSO));

        if (FormDataSOs.isSent(formDataSO)) {
            logger.error("Inconsistency check failed on calling [sendFormData]: FormData is already sent.");
            return ResponseFactory.preconditionFailed("FormData is already sent.");
        }

        if (!FormDataSOs.isExercise(formDataSO)) {
            logger.error("Inconsistency check failed on calling [sendFormData]: FormData is no exercise.");
            return ResponseFactory.preconditionFailed("FormData is no exercise.");
        }

        // TODO: Check if contentId is consistent and exercise

        ExerciseSO exerciseSO = formDataSO.getExercise();
        exerciseSO.setSent(true);
        exerciseSO.setLastModifiedTimestamp(Dates.currentTimestampAsISO());

        Document document = FormDataConverter.convert(formDataSO);
        FormDataMongoHandler.createOrUpdate(document);

        logger.debug(document.toJson());

        // TODO
        new ExerciseSubmittedEvent(formDataSO).fire();

        return ResponseFactory.ok();
    }

    @GET
    @Path("feedbackData/{contentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public FeedbackData getFeedbackData(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        logger.debug("[feedbackData]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);
        UserVO userVO = patientHttpSessionAttribute.getUserVO();

        Document document = FeedbackDataMongoHandler.fetch(userVO.getId(), contentId);
        if (document == null) return new FeedbackData();

        FeedbackData feedbackData = FeedbackDataConverter.convert(document);

        logger.debug(document.toJson());

        return feedbackData;
    }

}
