package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import org.mentalizr.backend.security.auth.AuthorizationService;
import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUser;
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
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, InfrastructureException {
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
                        therapistRestoreSO.getFirstActive(),
                        therapistRestoreSO.getLastActive(),
                        therapistRestoreSO.getPolicyConsent(),
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

        }.call();

    }

}
