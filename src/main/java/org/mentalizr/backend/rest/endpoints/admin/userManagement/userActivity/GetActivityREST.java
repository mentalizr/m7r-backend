package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.serviceObjects.requestObjects.ActivityQuerySO;
import org.mentalizr.serviceObjects.userManagement.ActivityStatusMessageCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
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
            protected ActivityStatusMessageCollectionSO workLoad() {

                // TODO Hier Exception prüfen user == null
                // TODO Hier prüfen until >= from

                return ActivityStatusMessageConverter
                        .convertDocumentListToCollection(
                                ActivityStatusMessageMongoHandler.fetchAllOfUserIDBetween(
                                        activityQuerySO.getUserId(),
                                        activityQuerySO.getFromTimestamp(),
                                        activityQuerySO.getUntilTimestamp()));
//
//
//                if(!activityQuerySO.getUserId().isEmpty()) {
//                    if (activityQuerySO.getFromTimestamp() != 0 && activityQuerySO.getUntilTimestamp() != 0) {
//                        collectionSO = ActivityStatusMessageConverter
//                                .convertDocumentListToCollection(
//                                        ActivityStatusMessageMongoHandler.fetchAllOfUserIDBetween(
//                                                activityQuerySO.getUserId(),
//                                                activityQuerySO.getFromTimestamp(),
//                                                activityQuerySO.getUntilTimestamp()));
//
//                    } else if (activityQuerySO.getFromTimestamp() != 0 && activityQuerySO.getUntilTimestamp() == 0) {
//                        collectionSO = ActivityStatusMessageConverter
//                                .convertDocumentListToCollection(
//                                        ActivityStatusMessageMongoHandler.fetchAllOfUserIDFrom(
//                                                activityQuerySO.getUserId(),
//                                                activityQuerySO.getFromTimestamp()));
//
//                    } else if (activityQuerySO.getUntilTimestamp() != 0 && activityQuerySO.getFromTimestamp() == 0) {
//                        collectionSO = ActivityStatusMessageConverter.convertDocumentListToCollection(
//                                ActivityStatusMessageMongoHandler.fetchAllOfUserIDUntil(
//                                        activityQuerySO.getUserId(),
//                                        activityQuerySO.getUntilTimestamp()));
//
//                    } else if (activityQuerySO.getFromTimestamp() == 0 && activityQuerySO.getUntilTimestamp() == 0) {
//                        collectionSO = ActivityStatusMessageConverter
//                                .convertDocumentListToCollection(
//                                        ActivityStatusMessageMongoHandler.fetchAllOfUserID(activityQuerySO.getUserId()));
//                    }
//                } else {
//                    // TODO Exception wenn keine User-Id gegeben.
//                    throw new RuntimeException("TODO ...");
//                }

//                return collectionSO;
            }

            @Override
            protected void updateActivityStatus() {

            }
        }.call();

    }

}
