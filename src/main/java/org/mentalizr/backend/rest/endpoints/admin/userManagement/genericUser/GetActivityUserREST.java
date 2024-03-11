package org.mentalizr.backend.rest.endpoints.admin.userManagement.genericUser;

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
import org.mentalizr.serviceObjects.requestObjects.ActivityCommandSO;
import org.mentalizr.serviceObjects.userManagement.ActivityStatusMessageCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class GetActivityUserREST {

    private static final String SERVICE_ID = "admin/user/patient/activity";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activity(ActivityCommandSO activityCommandSO,
                             @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest , activityCommandSO) {
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
            protected void checkPreconditions() throws M7rInfrastructureException, ServicePreconditionFailedException {

            }

            @Override
            protected ActivityStatusMessageCollectionSO workLoad() {
                ActivityStatusMessageCollectionSO collectionSO = null;

                if (activityCommandSO.getFromTimestamp() != 0 && activityCommandSO.getUntilTimestamp() != 0) {
                    collectionSO = ActivityStatusMessageConverter
                            .convertDocumentListToCollection(
                                    ActivityStatusMessageMongoHandler.fetchAllOfUserIDBetween(
                                            activityCommandSO.getUserId(),
                                            activityCommandSO.getFromTimestamp(),
                                            activityCommandSO.getUntilTimestamp()));

                } else if (activityCommandSO.getFromTimestamp() != 0 && activityCommandSO.getUntilTimestamp() == 0) {
                    collectionSO = ActivityStatusMessageConverter
                            .convertDocumentListToCollection(
                                    ActivityStatusMessageMongoHandler.fetchAllOfUserIDFrom(
                                            activityCommandSO.getUserId(),
                                            activityCommandSO.getFromTimestamp()));

                } else if (activityCommandSO.getUntilTimestamp() != 0 && activityCommandSO.getFromTimestamp() == 0) {
                    collectionSO = ActivityStatusMessageConverter.convertDocumentListToCollection(
                            ActivityStatusMessageMongoHandler.fetchAllOfUserIDUntil(
                                            activityCommandSO.getUserId(),
                                            activityCommandSO.getUntilTimestamp()));

                } else if (activityCommandSO.getFromTimestamp() == 0 && activityCommandSO.getUntilTimestamp() == 0) {
                    collectionSO = ActivityStatusMessageConverter
                            .convertDocumentListToCollection(
                                    ActivityStatusMessageMongoHandler.fetchAllOfUserID(activityCommandSO.getUserId()));
                }
                return collectionSO;
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject("userid: " + activityCommandSO.getUserId())));
            }
        }.call();

    }

}
