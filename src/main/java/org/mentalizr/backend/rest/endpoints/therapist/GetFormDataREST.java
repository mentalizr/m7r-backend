package org.mentalizr.backend.rest.endpoints.therapist;

import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOX;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.security.auth.AuthorizationService.assertIsLoggedInAsTherapist;

@Path("v1")
public class GetFormDataREST {

    private static final String SERVICE_ID = "therapist/formData";

    @GET
    @Path(SERVICE_ID + "/{userId}/{contentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFormData(
            @PathParam("userId") String userId,
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return assertIsLoggedInAsTherapist(httpServletRequest);
            }

            @Override
            protected FormDataSO workLoad() {
                FormDataSO formDataSO = FormDataDAO.obtain(userId, contentId);
                logger.trace(FormDataSOX.toJsonWithFormatting(formDataSO));
                return formDataSO;
            }

            @Override
            protected void logLeave() {
                this.logger.debug("[" + SERVICE_ID + "][" + userId + "][" + contentId + "] completed.");
            }

        }.call();

    }

}
