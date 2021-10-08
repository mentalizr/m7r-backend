package org.mentalizr.backend.rest.endpoints;

import org.bson.Document;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.persistence.mongo.DocumentPreexistingException;
import org.mentalizr.persistence.mongo.formData.FormDataConverter;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.serviceObjects.backup.FormDataCollectionSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class EndpointAdminFormData {

    private static final Logger logger = LoggerFactory.getLogger(EndpointAdminFormData.class);

    @GET
    @Path("admin/formData/getAll/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @PathParam("userId") String userId,
            @Context HttpServletRequest httpServletRequest) {

        logger.info("[admin/formData/getAll] for [" + userId + "]");

        assertIsLoggedInAsAdmin(httpServletRequest);

        FormDataCollectionSO formDataCollectionSO = FormDataDAO.fetchAll(userId);

        return ResponseFactory.ok(formDataCollectionSO);
    }

    @POST
    @Path("admin/formData/restore")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response restore(FormDataSO formDataSO,
                                 @Context HttpServletRequest httpServletRequest) {

        logger.info("[admin/formData/restore]");

        assertIsLoggedInAsAdmin(httpServletRequest);

        Document document = FormDataConverter.convert(formDataSO);
        try {
            FormDataMongoHandler.restore(document);
            return ResponseFactory.ok();
        } catch (DocumentPreexistingException e) {
            return ResponseFactory.preconditionFailed(e.getMessage());
        }
    }

    @GET
    @Path("admin/formData/clean/{userId}")
    public Response clean(
            @PathParam("userId") String userId,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[admin/formData/clean] [" + userId + "]");

        assertIsLoggedInAsAdmin(httpServletRequest);

        FormDataMongoHandler.clean(userId);

        return ResponseFactory.ok();
    }

}
