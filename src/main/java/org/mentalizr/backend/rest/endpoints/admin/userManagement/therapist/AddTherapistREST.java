package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUserLogin;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.userAdmin.UserLogin;
import org.mentalizr.serviceObjects.userManagement.TherapistAddSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class AddTherapistREST {

    private static final String SERVICE_ID = "admin/user/therapist/add";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TherapistAddSO therapistAddSO,
                        @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, InfrastructureException {
                AssertUserLogin.existsNotWithUsername(
                        therapistAddSO.getUsername(),
                        "User with specified username [%s] is preexisting."
                );
            }

            @Override
            protected TherapistAddSO workLoad() throws DataSourceException {
                UserLoginCompositeVO userLoginCompositeVO = UserLogin.add(
                        therapistAddSO.isActive(),
                        therapistAddSO.getUsername(),
                        therapistAddSO.getPassword().toCharArray(),
                        therapistAddSO.getEmail(),
                        therapistAddSO.getFirstname(),
                        therapistAddSO.getLastname(),
                        therapistAddSO.getGender()
                );

                String userUUID = userLoginCompositeVO.getUserId();

                RoleTherapistVO roleTherapistVO = new RoleTherapistVO(userUUID);
                roleTherapistVO.setTitle(therapistAddSO.getTitle());
                RoleTherapistDAO.create(roleTherapistVO);

                therapistAddSO.setUserId(userUUID);
                therapistAddSO.setPasswordHash(userLoginCompositeVO.getPasswordHash());

                return therapistAddSO;
            }

        }.call();

    }

}
