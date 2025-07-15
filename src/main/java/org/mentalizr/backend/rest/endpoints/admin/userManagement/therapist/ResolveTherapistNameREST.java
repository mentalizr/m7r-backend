package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.serviceObjects.userManagement.UserIdSO;
import org.mentalizr.serviceObjects.userManagement.UserNameSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class ResolveTherapistNameREST {

    private static final String SERVICE_ID = "admin/user/therapist/resolveName";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resolveTherapistName(
            UserNameSO userNameSO,
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
            protected UserIdSO workLoad() throws DataSourceException, EntityNotFoundException {
                UserLoginVO userLoginVO = UserLoginDAO.findByUk_username(userNameSO.getUserName());
                RoleTherapistDAO.load(userLoginVO.getUserId());
                UserIdSO userIdSO = new UserIdSO();
                userIdSO.setId(userLoginVO.getUserId());
                return userIdSO;
            }

        }.call();
    }
}
