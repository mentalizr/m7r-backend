package org.mentalizr.backend.rest.endpoints.patient;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.config.BrandingConfiguration;
import org.mentalizr.backend.config.BrandingConfigurationFactory;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatient;

@Path("v1")
public class AppConfigREST {

    private static final String SERVICE_ID = "patient/appConfig";

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
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return assertIsLoggedInAsPatient(httpServletRequest);
            }

            @Override
            protected ApplicationConfigPatientSO workLoad() {
                BrandingConfiguration brandingConfiguration = ApplicationContext.getBrandingConfiguration();
                String programId = getPatientHttpSessionAttribute().getPatientProgramVO().getProgramId();
                return brandingConfiguration.getApplicationConfigPatientSO(programId);
            }

        }.call();
    }

}
