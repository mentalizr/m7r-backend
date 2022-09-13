package org.mentalizr.backend.rest.endpoints.admin.userManagement.patient;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUser;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUserLogin;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramPK;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.userAdmin.UserLogin;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("v1")
public class RestorePatientREST {

    private static final String SERVICE_ID = "admin/user/patient/restore";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(PatientRestoreSO patientRestoreSO,
                            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest, patientRestoreSO) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, InfrastructureException {
                AssertUser.existsNot(
                        patientRestoreSO.getUserId(),
                        "User with specified UUID [%s] is preexisting."
                );

                AssertUserLogin.existsNotWithUsername(
                        patientRestoreSO.getUsername(),
                        "User with specified username [%s] preexisting."
                );

                AssertRoleTherapist.exists(
                        patientRestoreSO.getTherapistId(),
                        "Referenced therapist [%s] does not exist.");

                AssertProgram.exists(
                        patientRestoreSO.getProgramId(),
                        "Referenced program [%s] does not exist.");
            }

            @Override
            protected Object workLoad() throws DataSourceException, EntityNotFoundException, RESTException, ContentManagerException, IOException {
                UserLogin.restore(
                        patientRestoreSO.getUserId(),
                        patientRestoreSO.isActive(),
                        patientRestoreSO.getFirstActive(),
                        patientRestoreSO.getLastActive(),
                        patientRestoreSO.getPolicyConsent(),
                        patientRestoreSO.getUsername(),
                        patientRestoreSO.getPasswordHash(),
                        patientRestoreSO.getEmail(),
                        patientRestoreSO.getFirstname(),
                        patientRestoreSO.getLastname(),
                        patientRestoreSO.getGender(),
                        patientRestoreSO.isSecondFA(),
                        patientRestoreSO.getEmailConfirmation(),
                        patientRestoreSO.getEmailConfToken(),
                        patientRestoreSO.getEmailConfCode(),
                        patientRestoreSO.isRenewPasswordRequired()
                );

                RolePatientVO rolePatientVO = new RolePatientVO(patientRestoreSO.getUserId());
                rolePatientVO.setTherapistId(patientRestoreSO.getTherapistId());
                RolePatientDAO.create(rolePatientVO);

                PatientProgramPK patientProgramPK
                        = new PatientProgramPK(patientRestoreSO.getUserId(), patientRestoreSO.getProgramId());
                PatientProgramVO patientProgramVO = new PatientProgramVO(patientProgramPK);
                patientProgramVO.setBlocking(patientRestoreSO.isBlocking());
                PatientProgramDAO.create(patientProgramVO);

                return null;
            }

        }.call();

    }

}
