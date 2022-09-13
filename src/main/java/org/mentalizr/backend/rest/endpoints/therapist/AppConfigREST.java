package org.mentalizr.backend.rest.endpoints.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.BrandingConfiguration;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.serviceObjects.frontend.therapist.ApplicationConfigTherapistSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("v1")
public class AppConfigREST {

    private static final String SERVICE_ID = "therapist/appConfig";

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
                return AccessControl.assertValidSession(Therapist.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected ApplicationConfigTherapistSO workLoad() {
                BrandingConfiguration brandingConfiguration = ApplicationContext.getBrandingConfiguration();
                return brandingConfiguration.getApplicationConfigTherapistSO();
            }

        }.call();
    }

}
