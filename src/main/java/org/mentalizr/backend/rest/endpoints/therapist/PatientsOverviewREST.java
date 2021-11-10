package org.mentalizr.backend.rest.endpoints.therapist;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.TherapistHttpSessionAttribute;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.patientsOverviewSOCreator.PatientsOverviewSOCreator;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.serviceObjects.frontend.therapist.PatientsOverviewSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsTherapist;

@Path("v1")
public class PatientsOverviewREST {

    private static final String SERVICE_ID = "therapist/patientsOverview";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatientsOverview(@Context HttpServletRequest httpServletRequest) {

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
            protected PatientsOverviewSO workLoad() {
                TherapistHttpSessionAttribute therapistHttpSessionAttribute
                        = assertIsLoggedInAsTherapist(httpServletRequest);
                PatientsOverviewSOCreator patientsOverviewSOCreator
                        = new PatientsOverviewSOCreator(therapistHttpSessionAttribute.getRoleTherapistVO());
                return patientsOverviewSOCreator.create();
            }

        }.call();

    }

}
