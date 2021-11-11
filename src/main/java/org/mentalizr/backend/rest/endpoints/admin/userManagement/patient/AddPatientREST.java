package org.mentalizr.backend.rest.endpoints.admin.userManagement.patient;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUserLogin;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramPK;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.userAdmin.UserLogin;
import org.mentalizr.serviceObjects.userManagement.PatientAddSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class AddPatientREST {

    private static final String SERVICE_ID = "admin/user/patient/add";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(PatientAddSO patientAddSO,
                        @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest, patientAddSO) {

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
                AssertUserLogin.existsNotWithUsername(
                        patientAddSO.getUsername(),
                        "User with specified username [%s] is preexisting."
                );
                AssertRoleTherapist.exists(
                        patientAddSO.getTherapistId(),
                        "Referenced therapist [%s] does not exist.");
                AssertProgram.exists(
                        patientAddSO.getProgramId(),
                        "Referenced program [%s] does not exits.");
            }

            @Override
            protected PatientAddSO workLoad() throws DataSourceException {
                UserLoginCompositeVO userLoginCompositeVO = UserLogin.add(
                        patientAddSO.isActive(),
                        patientAddSO.getUsername(),
                        patientAddSO.getPassword().toCharArray(),
                        patientAddSO.getEmail(),
                        patientAddSO.getFirstname(),
                        patientAddSO.getLastname(),
                        patientAddSO.getGender()
                );

                String userUUID = userLoginCompositeVO.getUserId();

                RolePatientVO rolePatientVO = new RolePatientVO(userUUID);
                rolePatientVO.setTherapistId(patientAddSO.getTherapistId());
                RolePatientDAO.create(rolePatientVO);

                PatientProgramVO patientProgramVO =
                        new PatientProgramVO(new PatientProgramPK(userUUID, patientAddSO.getProgramId()));
                patientProgramVO.setBlocking(patientAddSO.isBlocking());
                PatientProgramDAO.create(patientProgramVO);

                patientAddSO.setUserId(userUUID);
                patientAddSO.setPasswordHash(userLoginCompositeVO.getPasswordHash());

                return patientAddSO;
            }

        }.call();

    }

}
