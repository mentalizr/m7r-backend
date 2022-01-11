package org.mentalizr.backend.rest.endpoints.generic;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.config.BrandingConfiguration;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class AppConfigGenericREST {

    private static final String SERVICE_ID = "generic/appConfigGeneric";

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
            protected UserHttpSessionAttribute checkSecurityConstraints() {
                return null;
            }

            @Override
            protected ApplicationConfigGenericSO workLoad() {
                BrandingConfiguration brandingConfiguration = ApplicationContext.getBrandingConfiguration();
                return brandingConfiguration.getApplicationConfigGenericSO();
            }

        }.call();
    }

}
