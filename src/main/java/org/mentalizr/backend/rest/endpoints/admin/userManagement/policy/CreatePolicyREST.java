package org.mentalizr.backend.rest.endpoints.admin.userManagement.policy;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertPolicy;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.userAdmin.Policy;
import org.mentalizr.serviceObjects.userManagement.PolicySO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class CreatePolicyREST {

    private static final String SERVICE_ID = "/admin/user/policy/create";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PolicySO policySO,
                           @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest){

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, M7rInfrastructureException {
                AssertPolicy.notExisting(policySO.getUserId(), policySO.getVersion());
            }

            @Override
            protected Object workLoad() throws DataSourceException {
                Policy.create(policySO.getUserId(), policySO.getVersion(), policySO.getConsent());
                return null;
            }

        }.call();

    }

}
