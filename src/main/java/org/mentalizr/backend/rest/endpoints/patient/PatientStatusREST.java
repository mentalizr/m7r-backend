package org.mentalizr.backend.rest.endpoints.patient;

import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusDAO;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class PatientStatusREST {

    private static final String SERVICE_ID = "patient/patientStatus";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response appConfig(@Context HttpServletRequest httpServletRequest) {

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
            protected PatientStatusSO workLoad() {
                String userId = this.authorization.getUserId();
                return PatientStatusDAO.obtain(userId);
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject()));
            }

        }.call();
    }

}
