package org.mentalizr.backend.rest.endpoints.therapist;

import org.mentalizr.backend.auth.TherapistHttpSessionAttribute;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.patientMessagesSOCreator.PatientMessagesSOCreator;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.serviceObjects.frontend.therapist.patientMessage.PatientMessagesSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsTherapist;

@Path("v1")
public class GetPatientMessagesREST {

    public static final String SERVICE_ID = "therapist/patientMessages";

    @GET
    @Path(SERVICE_ID + "/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatientMessages(
            @PathParam("patientId") String patientId,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() {
                return assertIsLoggedInAsTherapist(httpServletRequest);
            }

            @Override
            protected PatientMessagesSO workLoad() {
                TherapistHttpSessionAttribute therapistHttpSessionAttribute
                        = assertIsLoggedInAsTherapist(httpServletRequest);
                String therapistId = therapistHttpSessionAttribute.getUserVO().getId();

                PatientMessagesSOCreator patientMessagesSOCreator = new PatientMessagesSOCreator(patientId, therapistId);
                return patientMessagesSOCreator.create();
            }

            @Override
            protected void logLeave() {
                String userId = this.userHttpSessionAttribute.getUserVO().getId();
                this.logger.debug("[" + SERVICE_ID + "][" + userId + "][" + patientId + "] completed.");
            }

        }.call();

    }

}
