package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rBusinessConstraintException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.edao.PolicyConsentEDAO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("v1")
public class DeleteTherapistREST {

    private static final String SERVICE_ID = "admin/user/therapist/delete";

    @GET
    @Path(SERVICE_ID + "/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @PathParam("username") String username,
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
            protected Object workLoad() throws DataSourceException, EntityNotFoundException, M7rBusinessConstraintException {
                UserLoginVO userLoginVO = UserLoginDAO.findByUk_username(username);
                RoleTherapistVO roleTherapistVO = RoleTherapistDAO.load(userLoginVO.getUserId());

                List<RolePatientVO> rolePatientVOList = roleTherapistVO.getRolePatientVOByFk_therapist_id();
                if (!rolePatientVOList.isEmpty())
                    throw new M7rBusinessConstraintException("Therapist has dependent patients.");

                RoleTherapistDAO.delete(userLoginVO.getUserId());
                UserLoginDAO.delete(userLoginVO.getUserId());
                PolicyConsentEDAO.deleteAllForUser(userLoginVO.getUserId());
                UserDAO.delete(userLoginVO.getUserId());

                return null;
            }

        }.call();

    }

}
