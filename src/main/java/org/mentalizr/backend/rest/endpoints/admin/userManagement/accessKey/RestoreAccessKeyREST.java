package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyRestore;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class RestoreAccessKeyREST {

    private static final String SERVICE_ID = "admin/user/accessKey/restore";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(AccessKeyRestoreSO accessKeyRestoreSO,
                            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest, accessKeyRestoreSO){

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
                        getAccessKeyRestoreSO().getTherapistId(),
                        "Referenced therapist [%s] not existing."
                );
                AssertProgram.exists(
                        getAccessKeyRestoreSO().getProgramId(),
                        "Referenced program [%s] not existing."
                );
            }

            @Override
            protected Object workLoad() throws InfrastructureException {
                PatientAccessKeyRestore.restore(getAccessKeyRestoreSO());
                return null;
            }

            private AccessKeyRestoreSO getAccessKeyRestoreSO() {
                return (AccessKeyRestoreSO) this.serviceObjectRequest;
            }

        }.call();

    }

}
