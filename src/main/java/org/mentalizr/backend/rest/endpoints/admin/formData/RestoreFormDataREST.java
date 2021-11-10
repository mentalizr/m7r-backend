package org.mentalizr.backend.rest.endpoints.admin.formData;

import org.bson.Document;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.mongo.DocumentPreexistingException;
import org.mentalizr.persistence.mongo.formData.FormDataConverter;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class RestoreFormDataREST {

    private static final String SERVICE_ID = "admin/formData/restore";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(FormDataSO formDataSO,
                            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() {
                return assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected Object workLoad() throws RESTException {
                Document document = FormDataConverter.convert(formDataSO);
                try {
                    FormDataMongoHandler.restore(document);
                } catch (DocumentPreexistingException e) {
                    throw new RESTException("REST method [" + SERVICE_ID + "]. FormData cannot be restored as it is preexisting.");
                }
                return null;
            }

        }.call();

    }

}
