package org.mentalizr.backend.rest.endpoints.patient;

import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.serviceObjects.frontend.patient.ApplicationConfigPatientSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return M7rAccessControl.assertValidSessionAsPatientAbstract(this.httpServletRequest);
            }

            @Override
            protected ApplicationConfigPatientSO workLoad() {
                InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
                M7rAuthorization m7rAuthorization = new M7rAuthorization(this.authorization);
                String programId = m7rAuthorization.getUserAsPatientAbstract().getPatientProgramVO().getProgramId();
                return instanceConfiguration.getApplicationConfigPatientSO(programId);
            }

        }.call();
    }

}
