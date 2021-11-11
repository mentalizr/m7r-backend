package org.mentalizr.backend.rest.endpoints.admin.userManagement.patient;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class DeletePatientREST {

    private static final String SERVICE_ID = "admin/user/patient/delete";

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
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected Object workLoad() throws DataSourceException, EntityNotFoundException {
                UserLoginVO userLoginVO = UserLoginDAO.findByUk_username(username);
                PatientProgramVO patientProgramVO = PatientProgramDAO.findByUk_user_id(userLoginVO.getUserId());

                PatientProgramDAO.delete(patientProgramVO.getPK());
                RolePatientDAO.delete(userLoginVO.getUserId());
                UserLoginDAO.delete(userLoginVO.getUserId());
                UserDAO.delete(userLoginVO.getUserId());

                return null;
            }

        }.call();

    }

}
