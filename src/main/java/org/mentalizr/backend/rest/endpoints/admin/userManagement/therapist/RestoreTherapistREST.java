package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUser;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.userAdmin.UserLogin;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class RestoreTherapistREST {

    private static final String SERVICE_ID = "admin/user/therapist/restore";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(TherapistRestoreSO therapistRestoreSO,
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
            protected void checkPreconditions() throws ServicePreconditionFailedException, M7rInfrastructureException {
                AssertUser.existsNot(
                        therapistRestoreSO.getUserId(),
                        "User with specified UUID [%s] is preexisting."
                );
            }

            @Override
            protected Object workLoad() throws DataSourceException {
                UserLogin.restore(
                        therapistRestoreSO.getUserId(),
                        therapistRestoreSO.isActive(),
                        therapistRestoreSO.getCreation(),
                        therapistRestoreSO.getFirstActive(),
                        therapistRestoreSO.getLastActive(),
                        therapistRestoreSO.getUsername(),
                        therapistRestoreSO.getPasswordHash(),
                        therapistRestoreSO.getEmail(),
                        therapistRestoreSO.getFirstname(),
                        therapistRestoreSO.getLastname(),
                        therapistRestoreSO.getGender(),
                        therapistRestoreSO.isSecondFA(),
                        therapistRestoreSO.getEmailConfirmation(),
                        therapistRestoreSO.getEmailConfToken(),
                        therapistRestoreSO.getEmailConfCode(),
                        therapistRestoreSO.isRenewPasswordRequired()
                );
                RoleTherapistVO roleTherapistVO = new RoleTherapistVO(therapistRestoreSO.getUserId());
                RoleTherapistDAO.create(roleTherapistVO);
                return null;
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject("username: "
                                + therapistRestoreSO.getUsername() + " | userid: "
                                + therapistRestoreSO.getUserId())));
            }

        }.call();

    }

}
