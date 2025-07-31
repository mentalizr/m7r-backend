package org.mentalizr.backend.rest.endpoints.admin.patientStatus;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusMongoHandler;
import org.mentalizr.serviceObjects.userManagement.UserIDCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Set;

@Path("v1")
public class GetUserPatientStatusREST {

    private static final String SERVICE_ID = "admin/patientStatus/getUser";

    @POST
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(
            @Context HttpServletRequest httpServletRequest) {

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
            protected UserIDCollectionSO workLoad() {
                Set<String> distinctUserIds = PatientStatusMongoHandler.getDistinctUserIds();
                UserIDCollectionSO userIDCollection = new UserIDCollectionSO();
                userIDCollection.setCollection(new ArrayList<>(distinctUserIds));
                return userIDCollection;
            }

        }.call();

    }

}
