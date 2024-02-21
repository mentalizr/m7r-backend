package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyCreate;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
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
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, M7rInfrastructureException {
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
            protected AccessKeyCollectionSO workLoad() throws M7rInfrastructureException {
                return PatientAccessKeyCreate.create(getAccessKeyCreateSO());
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject("CREATED ACCESSKEYS { program: "
                                + accessKeyCreateSO.getProgramId() + " amount: "
                                + accessKeyCreateSO.getNrOfKeys() + " }")));
            }

            private AccessKeyCreateSO getAccessKeyCreateSO() {
                return (AccessKeyCreateSO) this.serviceObjectRequest;
            }

        }.call();

    }

}
