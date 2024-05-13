package org.mentalizr.backend.rest.endpoints.generic;

import de.arthurpicht.webAccessControl.auth.Authorization;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
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
            protected Authorization checkSecurityConstraints() {
                return null;
            }

            @Override
            protected ApplicationConfigGenericSO workLoad() {
                InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
                return instanceConfiguration.getApplicationConfigGenericSO();
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject()));
            }

        }.call();
    }

}
