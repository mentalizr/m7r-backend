package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rIllegalServiceInputException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageMongoHandler;
import org.mentalizr.serviceObjects.requestObjects.ActivityQuerySO;
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
public class GetActivityREST {

    private static final String SERVICE_ID = "admin/user/activity/get";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activity(ActivityQuerySO activityQuerySO,
                             @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest , activityQuerySO) {
            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints()
                    throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected ActivityStatusMessageCollectionSO workLoad() throws M7rIllegalServiceInputException {
                if(activityQuerySO.getUserId() == null || activityQuerySO.getUserId().isEmpty()) {
                    throw new M7rIllegalServiceInputException("UserId not set.");
                } else if (activityQuerySO.getUntilTimestamp() < activityQuerySO.getFromTimestamp()) {
                    throw new M7rIllegalServiceInputException("From timestamp is less than until timestamp.");
                }

                return ActivityMessageConverter
                        .convertDocumentListToCollection(
                                ActivityMessageMongoHandler.fetchAllOfUserIDBetween(
                                        activityQuerySO.getUserId(),
                                        activityQuerySO.getFromTimestamp(),
                                        activityQuerySO.getUntilTimestamp()
                                )
                        );
            }

            @Override
            protected void updateActivityStatus() {}

        }.call();

    }

}
