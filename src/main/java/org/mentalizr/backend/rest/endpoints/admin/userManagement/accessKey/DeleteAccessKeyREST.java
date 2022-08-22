package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import org.mentalizr.backend.security.auth.AuthorizationService;
import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertAccessKey;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyDelete;
import org.mentalizr.serviceObjects.userManagement.AccessKeyDeleteSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class DeleteAccessKeyREST {

    private static final String SERVICE_ID = "admin/user/accessKey/delete";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            AccessKeyDeleteSO accessKeyDeleteSO,
            @Context HttpServletRequest httpServletRequest
    ) {

        return new Service(httpServletRequest, accessKeyDeleteSO){

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return AuthorizationService.assertIsLoggedInAsAdmin(this.httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, InfrastructureException {
                AssertAccessKey.isExistingWithAccessKey(getAccessKeyDeleteSO().getAccessKey());
            }

            @Override
            protected Object workLoad() throws InfrastructureException {
                PatientAccessKeyDelete.delete(getAccessKeyDeleteSO());
                return null;
            }

            private AccessKeyDeleteSO getAccessKeyDeleteSO() {
                return (AccessKeyDeleteSO) this.serviceObjectRequest;
            }

        }.call();

    }

}
