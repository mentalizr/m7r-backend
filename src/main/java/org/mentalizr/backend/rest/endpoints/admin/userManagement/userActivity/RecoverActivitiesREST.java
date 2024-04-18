package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.serviceObjects.userManagement.ActivityStatusMessageCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class RecoverActivitiesREST {

    private static final String SERVICE_ID = "/admin/user/activity/add";

    @POST
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(ActivityStatusMessageCollectionSO activityStatusMessageCollectionSO, @Context HttpServletRequest httpServletRequest) {
        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected ActivityStatusMessageCollectionSO workLoad() {
                ActivityStatusMessageMongoHandler.insertMany(
                        ActivityStatusMessageConverter
                                .convertActivityList(activityStatusMessageCollectionSO.getCollection()));
                return activityStatusMessageCollectionSO;
            }

            @Override
            protected void updateActivityStatus() {

            }
        }.call();
    }
}
