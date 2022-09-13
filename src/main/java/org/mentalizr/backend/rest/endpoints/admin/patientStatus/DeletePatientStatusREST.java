package org.mentalizr.backend.rest.endpoints.admin.patientStatus;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusMongoHandler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("v1")
public class DeletePatientStatusREST {

    private static final String SERVICE_ID = "admin/patientStatus/delete";

    @GET
    @Path(SERVICE_ID + "/{userId}")
    public Response clean(
            @PathParam("userId") String userId,
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
            protected Object workLoad() {
                PatientStatusMongoHandler.delete(userId);
                return null;
            }

        }.call();

    }

}
