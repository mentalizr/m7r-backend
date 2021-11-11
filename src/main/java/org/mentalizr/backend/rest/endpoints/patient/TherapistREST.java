package org.mentalizr.backend.rest.endpoints.patient;

import org.mentalizr.backend.auth.PatientHttpSessionAttribute;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.entities.UserFactory;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.serviceObjects.frontend.application.UserSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatient;

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
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return assertIsLoggedInAsPatient(this.httpServletRequest);
            }

            @Override
            protected UserSO workLoad() {
                PatientHttpSessionAttribute patientHttpSessionAttribute = getPatientHttpSessionAttribute();
                return UserFactory.getInstanceForRelatedTherapist(patientHttpSessionAttribute);
            }

        }.call();

    }

}
