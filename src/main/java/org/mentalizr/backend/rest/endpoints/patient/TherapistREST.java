package org.mentalizr.backend.rest.endpoints.patient;

import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.accessControl.roles.PatientAbstract;
import org.mentalizr.backend.rest.entities.UserFactory;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.serviceObjects.frontend.application.UserSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class TherapistREST {

    private static final String SERVICE_ID = "patient/therapist";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response therapist(@Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return M7rAccessControl.assertValidSessionAsPatientAbstract(this.httpServletRequest);
            }

            @Override
            protected UserSO workLoad() {
                M7rAuthorization m7rAuthorization = new M7rAuthorization(this.authorization);
                PatientAbstract patientAbstract = m7rAuthorization.getUserAsPatientAbstract();
                return UserFactory.getInstanceForRelatedTherapist(patientAbstract);
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject()));
            }

        }.call();

    }

}
