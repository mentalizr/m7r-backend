package org.mentalizr.backend.rest.endpoints.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.accessControl.roles.Therapist;
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
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Therapist.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected PatientsOverviewSO workLoad() {
                M7rAuthorization m7rAuthorization = new M7rAuthorization(this.authorization);
                Therapist therapist = m7rAuthorization.getUserAsTherapist();
                PatientsOverviewSOCreator patientsOverviewSOCreator
                        = new PatientsOverviewSOCreator(therapist.getRoleTherapistVO());
                return patientsOverviewSOCreator.create();
            }

        }.call();

    }

}
