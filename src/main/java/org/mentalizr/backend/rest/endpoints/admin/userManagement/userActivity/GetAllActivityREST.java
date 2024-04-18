package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rIllegalServiceInputException;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.serviceObjects.userManagement.ActivityStatusMessageCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class GetAllActivityREST {

    private static final String SERVICE_ID = "admin/user/activity/get/all";

    @GET
    @Path(SERVICE_ID + "/{userid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("userid") String userid,
                           @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {
            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints()
                    throws UnauthorizedException, M7rIllegalServiceInputException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected void checkPreconditions()
                    throws M7rInfrastructureException, ServicePreconditionFailedException {

            }
            @Override
            protected ActivityStatusMessageCollectionSO workLoad() {
                return ActivityStatusMessageConverter
                        .convertDocumentListToCollection(
                                ActivityStatusMessageMongoHandler.fetchAllOfUserID(userid));
            }

            @Override
            protected void updateActivityStatus() {

            }
        }.call();
    }
}
