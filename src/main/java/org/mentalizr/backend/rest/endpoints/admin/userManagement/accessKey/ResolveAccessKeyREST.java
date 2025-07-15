package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeySO;
import org.mentalizr.serviceObjects.userManagement.UserIdSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class ResolveAccessKeyREST {

    private static final String SERVICE_ID = "admin/user/accessKey/resolve";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resolveAccessKey(
            AccessKeySO accessKeySO,
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
                UserAccessKeyVO userAccessKeyVO = UserAccessKeyDAO.findByUk_accessKey(accessKeySO.getAccessKey());
                UserIdSO userIdSO = new UserIdSO();
                userIdSO.setId(userAccessKeyVO.getUserId());
                return userIdSO;
            }

        }.call();
    }
}
