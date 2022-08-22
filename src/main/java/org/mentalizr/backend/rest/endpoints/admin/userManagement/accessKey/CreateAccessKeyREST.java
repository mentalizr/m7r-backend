package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import org.mentalizr.backend.security.auth.AuthorizationService;
import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyCreate;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCreateSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class CreateAccessKeyREST {

    private static final String SERVICE_ID = "admin/user/accessKey/create";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(AccessKeyCreateSO accessKeyCreateSO,
                           @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest, accessKeyCreateSO) {

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
                AssertRoleTherapist.exists(
                        getAccessKeyCreateSO().getTherapistId(),
                        "Referenced therapist [%s] does not exist."
                );
                AssertProgram.exists(
                        getAccessKeyCreateSO().getProgramId(),
                        "Referenced program [%s] does not exist."
                );
            }

            @Override
            protected AccessKeyCollectionSO workLoad() throws InfrastructureException {
                return PatientAccessKeyCreate.create(getAccessKeyCreateSO());
            }

            private AccessKeyCreateSO getAccessKeyCreateSO() {
                return (AccessKeyCreateSO) this.serviceObjectRequest;
            }

        }.call();

    }

}
