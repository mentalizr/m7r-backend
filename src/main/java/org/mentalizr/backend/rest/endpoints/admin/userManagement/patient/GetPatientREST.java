package org.mentalizr.backend.rest.endpoints.admin.userManagement.patient;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("v1")
public class GetPatientREST {

    private static final String SERVICE_ID = "admin/user/patient/get";

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
            protected PatientRestoreSO workLoad() throws DataSourceException, EntityNotFoundException {
                UserLoginCompositeVO userLoginCompositeVO = UserLoginCompositeDAO.findByUk_username(username);
                String userId = userLoginCompositeVO.getUserId();
                RolePatientVO rolePatientVO = RolePatientDAO.load(userId);
                PatientProgramVO patientProgramVO = PatientProgramDAO.findByUk_user_id(userId);

                PatientRestoreSO patientRestoreSO = new PatientRestoreSO();
                patientRestoreSO.setUserId(userLoginCompositeVO.getUserId());
                patientRestoreSO.setActive(userLoginCompositeVO.isActive());
                Date firstActive = userLoginCompositeVO.getFirstActive();
                patientRestoreSO.setFirstActive(firstActive != null ? firstActive.toString() : null);
                Date lastActive = userLoginCompositeVO.getLastActive();
                patientRestoreSO.setLastActive(lastActive != null ? lastActive.toString() : null);
                patientRestoreSO.setUsername(userLoginCompositeVO.getUsername());
                patientRestoreSO.setPasswordHash(userLoginCompositeVO.getPasswordHash());
                patientRestoreSO.setEmail(userLoginCompositeVO.getEmail());
                patientRestoreSO.setFirstname(userLoginCompositeVO.getFirstName());
                patientRestoreSO.setLastname(userLoginCompositeVO.getLastName());
                patientRestoreSO.setGender(userLoginCompositeVO.getGender());

                patientRestoreSO.setProgramId(patientProgramVO.getProgramId());
                patientRestoreSO.setBlocking(patientProgramVO.getBlocking());
                patientRestoreSO.setTherapistId(rolePatientVO.getTherapistId());

                return patientRestoreSO;
            }

        }.call();

    }


}
