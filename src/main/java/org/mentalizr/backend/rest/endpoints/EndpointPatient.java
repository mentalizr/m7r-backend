package org.mentalizr.backend.rest.endpoints;

import org.bson.Document;
import org.mentalizr.backend.adapter.ProgramAdapter;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.PatientHttpSessionAttribute;
import org.mentalizr.backend.config.Configuration;
import org.mentalizr.backend.config.ProjectConfiguration;
import org.mentalizr.backend.proc.event.ExerciseSubmittedEvent;
import org.mentalizr.backend.rest.entities.UserFactory;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ContentNotFoundException;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ProgramNotFoundException;
import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackData;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataConverter;
import org.mentalizr.persistence.mongo.feedbackData.FeedbackDataMongoHandler;
import org.mentalizr.persistence.mongo.formData.*;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.frontend.application.UserSO;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;
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
    @Path("patient/program")
    @Produces(MediaType.APPLICATION_JSON)
    public ProgramSO program(@Context HttpServletRequest httpServletRequest) {
        logger.debug("[patient:program]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);

        PatientProgramVO patientProgramVO = patientHttpSessionAttribute.getPatientProgramVO();
        String programId = patientProgramVO.getProgramId();
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            ProgramStructure programStructure = contentManager.getProgramStructure(programId);
            ProgramSO programSO = ProgramAdapter.getProgramSO(programStructure);
            programSO.setBlocking(patientProgramVO.getBlocking());
            return programSO;
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
    public FormData getFormData(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        logger.debug("[formData] [" + contentId + "]");

        PatientHttpSessionAttribute patientHttpSessionAttribute = assertIsLoggedInAsPatient(httpServletRequest);
        UserVO userVO = patientHttpSessionAttribute.getUserVO();

        Document document = FormDataMongoHandler.fetch(userVO.getId(), contentId);
        if (document == null) return new FormData();

        FormData formData = FormDataConverter.convert(document);

        logger.debug(document.toJson());

        return formData;
    }

    @POST
    @Path("saveFormData")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveFormData(FormData formData,
                             @Context HttpServletRequest httpServletRequest) {

        logger.debug("[saveFormData] new");

        // TODO DEBUG
        logger.debug("FormData: " + formData.toString());

        PatientHttpSessionAttribute patientHttpSessionAttribute;

        patientHttpSessionAttribute = AuthorizationService.assertIsLoggedInAsPatientWithUserId(httpServletRequest, formData.getUserId());

        FormElementDataMap formElementDataMapUpdate = new FormElementDataMap(formData);

        UserVO userVO = patientHttpSessionAttribute.getUserVO();
        String contentId = formData.getContentId();

        Document documentPreexisting = FormDataMongoHandler.fetch(userVO.getId(), contentId);
        FormData formDataPreexisting = documentPreexisting == null ? new FormData() : FormDataConverter.convert(documentPreexisting);

        logger.debug("formDataPreexisting: " + formDataPreexisting);

        List<FormElementData> formElementDataListPreexisting = formDataPreexisting.getFormElementDataList();
        List<FormElementData> formElementDataListPost = new ArrayList<>();

        for (FormElementData formElementDataPreexisting : formElementDataListPreexisting) {

            String formElementId = formElementDataPreexisting.getFormElementId();
            boolean updateFormElementData = formElementDataMapUpdate.containsFormElementId(formElementId);

            if (updateFormElementData) {
                formElementDataListPost.add(formElementDataMapUpdate.getFormElementData(formElementId));
            } else {
                formElementDataListPost.add(formElementDataPreexisting);
            }
        }

        List<FormElementData> formElementDataListUpdate = formData.getFormElementDataList();
        FormElementDataMap formElementDataMapPreexisting = new FormElementDataMap(formDataPreexisting);
        for (FormElementData formElementDataUpdate : formElementDataListUpdate) {

            String formElementId = formElementDataUpdate.getFormElementId();
            boolean takeOver = !formElementDataMapPreexisting.containsFormElementId(formElementId);

            if (takeOver) {
                formElementDataListPost.add(formElementDataUpdate);
            }
        }

        FormData formDataPost = new FormData(
                formData.getUserId(),
                formData.getContentId(),
                formData.isEditable(),
                formElementDataListPost
        );

        Document document = FormDataConverter.convert(formDataPost);
        FormDataMongoHandler.createOrUpdate(document);

        logger.debug(document.toJson());
    }

    @POST
    @Path("sendFormData")
    @Consumes(MediaType.APPLICATION_JSON)
    public void sendFormData(FormData formData,
                             @Context HttpServletRequest httpServletRequest) {

        logger.debug("[sendFormData]");

        AuthorizationService.assertIsLoggedInAsPatientWithUserId(httpServletRequest, formData.getUserId());

        Document document = FormDataConverter.convert(formData);
        FormDataMongoHandler.createOrUpdate(document);

        logger.debug(document.toJson());

        new ExerciseSubmittedEvent(formData).fire();
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
