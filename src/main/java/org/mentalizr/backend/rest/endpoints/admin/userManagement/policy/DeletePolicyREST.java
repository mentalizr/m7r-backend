package org.mentalizr.backend.rest.endpoints.admin.userManagement.policy;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PolicyDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PolicyPK;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class DeletePolicyREST {

    private static final String SERVICE_ID = "/admin/user/policy/delete";

    @GET
    @Path(SERVICE_ID + "/{userId}/{version}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @PathParam("userId") String userId,
            @PathParam("version") String version,
            @Context HttpServletRequest httpServletRequest
    ) {

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
            protected Object workLoad() throws DataSourceException, EntityNotFoundException {
                PolicyPK policyPK = new PolicyPK(userId, version);
                PolicyDAO.load(policyPK);
                PolicyDAO.delete(policyPK);
                return null;
            }

        }.call();

    }

}
