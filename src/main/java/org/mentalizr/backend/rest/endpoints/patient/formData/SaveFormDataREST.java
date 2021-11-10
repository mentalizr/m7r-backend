package org.mentalizr.backend.rest.endpoints.patient.formData;

import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatientWithUserId;

@Path("v1")
public class SaveFormDataREST {

    private static final String SERVICE_ID = "patient/formData/save";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveFormData(FormDataSO formDataSO,
                                 @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() {
                return assertIsLoggedInAsPatientWithUserId(httpServletRequest, formDataSO.getUserId());
            }

            @Override
            protected Object workLoad() {
                FormDataMongoHandler.mergeWithPreexisting(formDataSO);
                return null;
            }

            @Override
            protected void logLeave() {
                String userId = formDataSO.getUserId();
                String contentId = formDataSO.getContentId();
                logger.debug("[" + SERVICE_ID + "][" + userId + "][" + contentId + "] completed.");
            }

        }.call();

    }

}
