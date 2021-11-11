package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("v1")
public class GetTherapistREST {

    private static final String SERVICE_ID = "admin/user/therapist/get";

    @GET
    @Path(SERVICE_ID + "/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
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
            protected TherapistRestoreSO workLoad() throws DataSourceException, EntityNotFoundException {
                UserLoginCompositeVO userLoginCompositeVO = UserLoginCompositeDAO.findByUk_username(username);
                RoleTherapistVO roleTherapistVO = RoleTherapistDAO.load(userLoginCompositeVO.getUserId());

                TherapistRestoreSO therapistRestoreSO = new TherapistRestoreSO();
                therapistRestoreSO.setUserId(userLoginCompositeVO.getUserId());
                therapistRestoreSO.setActive(userLoginCompositeVO.isActive());
                Date firstActive = userLoginCompositeVO.getFirstActive();
                therapistRestoreSO.setFirstActive(firstActive != null ? firstActive.toString() : null);
                Date lastActive = userLoginCompositeVO.getLastActive();
                therapistRestoreSO.setLastActive(lastActive != null ? lastActive.toString() : null);
                therapistRestoreSO.setUsername(userLoginCompositeVO.getUsername());
                therapistRestoreSO.setPasswordHash(userLoginCompositeVO.getPasswordHash());
                therapistRestoreSO.setEmail(userLoginCompositeVO.getEmail());
                therapistRestoreSO.setFirstname(userLoginCompositeVO.getFirstName());
                therapistRestoreSO.setLastname(userLoginCompositeVO.getLastName());
                therapistRestoreSO.setGender(userLoginCompositeVO.getGender());
                therapistRestoreSO.setTitle(roleTherapistVO.getTitle());

                return therapistRestoreSO;
            }

        }.call();

    }

}
