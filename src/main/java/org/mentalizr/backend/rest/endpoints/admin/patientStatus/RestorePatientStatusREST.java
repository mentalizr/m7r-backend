package org.mentalizr.backend.rest.endpoints.admin.patientStatus;

import org.bson.Document;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.DocumentPreexistingException;
import org.mentalizr.persistence.mongo.formData.FormDataConverter;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusConverter;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusMongoHandler;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class RestorePatientStatusREST {

    private static final String SERVICE_ID = "admin/patientStatus/restore";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(PatientStatusSO patientStatusSO,
                            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected Object workLoad() throws RESTException {
                Document document = PatientStatusConverter.convert(patientStatusSO);
                try {
                    PatientStatusMongoHandler.restore(document);
                } catch (DocumentPreexistingException e) {
                    throw new RESTException("REST method [" + SERVICE_ID + "]. PatientStatus cannot be restored as it is preexisting.");
                }
                return null;
            }

        }.call();

    }

}
