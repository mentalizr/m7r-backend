package org.mentalizr.backend.rest.endpoints;

import org.bson.Document;
import org.mentalizr.backend.adapter.ProgramAdapter;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.PatientHttpSessionAttribute;
import org.mentalizr.backend.config.Configuration;
import org.mentalizr.backend.config.ProjectConfiguration;
import org.mentalizr.backend.proc.event.ExerciseSubmittedEvent;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.backend.rest.entities.UserFactory;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ContentNotFoundException;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ProgramNotFoundException;
import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackData;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataConverter;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormDataConverter;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormElementDataMap;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.frontend.application.UserSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormElementDataSO;
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
import java.util.ArrayList;
import java.util.List;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatient;

@Path("v1")
public class EndpointPatient {

    private static final Logger logger = LoggerFactory.getLogger(EndpointPatient.class);
    private static final int DELAY_ON_NEXT_CONTENT = 0;

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
    @Path("program")
    @Produces(MediaType.APPLICATION_JSON)
    public ProgramSO program(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[program]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);

        PatientProgramVO patientProgramVO = patientHttpSessionAttribute.getPatientProgramVO();
        String programId = patientProgramVO.getProgramId();
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            ProgramStructure programStructure = contentManager.getProgramStructure(programId);
            return ProgramAdapter.getProgramSO(programStructure);
        } catch (ProgramNotFoundException e) {
            logger.error("Program not found. Cause: " + e.getMessage());
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("therapeutImgThumbnail")
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
    @Path("programContent/{contentId}")
    @Produces(MediaType.TEXT_HTML)
    public Response programContent(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[programContent] [" + contentId + "]");

        assertIsLoggedInAsPatient(httpServletRequest);

        return getProgramContent(contentId);
    }

    @GET
    @Path("programInfoContent/{contentId}")
    @Produces(MediaType.TEXT_HTML)
    public Response programInfoContent(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        //TODO This service is equal to 'programContent'. Unify to service 'content'.

        logger.debug("[programInfoContent] [" + contentId + "]");

        assertIsLoggedInAsPatient(httpServletRequest);

        return getProgramContent(contentId);
    }

    private Response getProgramContent(String contentId) {
        if (DELAY_ON_NEXT_CONTENT > 0) {
            try {
                Thread.sleep(DELAY_ON_NEXT_CONTENT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

    @GET
    @Path("formData/{contentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public FormDataSO getFormData(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        logger.debug("[formDataSO] [" + contentId + "]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);
        UserVO userVO = patientHttpSessionAttribute.getUserVO();

        Document document = FormDataMongoHandler.fetch(userVO.getUserId(), contentId);
        if (document == null) return new FormDataSO();

        FormDataSO formDataSO = FormDataConverter.convert(document);

        logger.debug(document.toJson());

        return formDataSO;
    }

    @POST
    @Path("saveFormData")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveFormData(FormDataSO formDataSO,
                             @Context HttpServletRequest httpServletRequest) {

        logger.debug("[saveFormData]");

        // TODO DEBUG
        logger.debug("FormDataSO: " + formDataSO.toString());

        PatientHttpSessionAttribute patientHttpSessionAttribute;

        patientHttpSessionAttribute = AuthorizationService.assertIsLoggedInAsPatientWithUserId(httpServletRequest, formDataSO.getUserId());

        FormElementDataMap formElementDataMapUpdate = new FormElementDataMap(formDataSO);

        UserVO userVO = patientHttpSessionAttribute.getUserVO();
        String contentId = formDataSO.getContentId();

        Document documentPreexisting = FormDataMongoHandler.fetch(userVO.getUserId(), contentId);
        FormDataSO formDataSOPreexisting = documentPreexisting == null ? new FormDataSO() : FormDataConverter.convert(documentPreexisting);

        logger.debug("formDataSOPreexisting: " + formDataSOPreexisting);

        List<FormElementDataSO> formElementDataSOListPreexisting = formDataSOPreexisting.getFormElementDataList();
        List<FormElementDataSO> formElementDataSOListPost = new ArrayList<>();

        for (FormElementDataSO formElementDataSOPreexisting : formElementDataSOListPreexisting) {

            String formElementId = formElementDataSOPreexisting.getFormElementId();
            boolean updateFormElementData = formElementDataMapUpdate.containsFormElementId(formElementId);

            if (updateFormElementData) {
                formElementDataSOListPost.add(formElementDataMapUpdate.getFormElementData(formElementId));
            } else {
                formElementDataSOListPost.add(formElementDataSOPreexisting);
            }
        }

        List<FormElementDataSO> formElementDataSOListUpdate = formDataSO.getFormElementDataList();
        FormElementDataMap formElementDataMapPreexisting = new FormElementDataMap(formDataSOPreexisting);
        for (FormElementDataSO formElementDataSOUpdate : formElementDataSOListUpdate) {

            String formElementId = formElementDataSOUpdate.getFormElementId();
            boolean takeOver = !formElementDataMapPreexisting.containsFormElementId(formElementId);

            if (takeOver) {
                formElementDataSOListPost.add(formElementDataSOUpdate);
            }
        }

        FormDataSO formDataSOPost = new FormDataSO(
                formDataSO.getUserId(),
                formDataSO.getContentId(),
                formDataSO.isEditable(),
                formElementDataSOListPost
        );

        Document document = FormDataConverter.convert(formDataSOPost);
        FormDataMongoHandler.createOrUpdate(document);

        logger.debug(document.toJson());

        return ResponseFactory.ok();
    }

    @POST
    @Path("sendFormData")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendFormData(FormDataSO formDataSO,
                             @Context HttpServletRequest httpServletRequest) {

        logger.debug("[sendFormData]");

        AuthorizationService.assertIsLoggedInAsPatientWithUserId(httpServletRequest, formDataSO.getUserId());

        Document document = FormDataConverter.convert(formDataSO);
        FormDataMongoHandler.createOrUpdate(document);

        logger.debug(document.toJson());

        new ExerciseSubmittedEvent(formDataSO).fire();
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

        Document document = FeedbackDataMongoHandler.fetch(userVO.getUserId(), contentId);
        if (document == null) return new FeedbackData();

        FeedbackData feedbackData = FeedbackDataConverter.convert(document);

        logger.debug(document.toJson());

        return feedbackData;
    }

}
