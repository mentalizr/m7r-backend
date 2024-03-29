package org.mentalizr.backend.rest.endpoints.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.accessControl.roles.Therapist;
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
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Therapist.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected PatientMessagesSO workLoad() {
                M7rAuthorization m7rAuthorization = new M7rAuthorization(this.authorization);
                Therapist therapist = m7rAuthorization.getUserAsTherapist();
                String therapistId = therapist.getUserVO().getId();

                PatientMessagesSOCreator patientMessagesSOCreator = new PatientMessagesSOCreator(patientId, therapistId);
                return patientMessagesSOCreator.create();
            }

            @Override
            protected void logLeave() {
                String userId = this.authorization.getUserId();
                this.logger.debug("[" + SERVICE_ID + "][" + userId + "][" + patientId + "] completed.");
            }

        }.call();

    }

}
