package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUser;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUserLogin;
import org.mentalizr.backend.rest.service.userManagement.loginPatient.LoginPatientGetAllService;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.userAdmin.UserLogin;
import org.mentalizr.serviceObjects.userManagement.PatientAddSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("v1/admin/user/patient")
public class EndpointUserManagementPatient {

    private static final Logger logger = LoggerFactory.getLogger(EndpointUserManagementPatient.class);

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(PatientAddSO patientAddSO,
                        @Context HttpServletRequest httpServletRequest) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:patient:add] username: [" + patientAddSO.getUsername() + "]");

        try {


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
            rolePatientVO.setProgramId(patientAddSO.getProgramId());
            rolePatientVO.setTherapistId(patientAddSO.getTherapistId());
            RolePatientDAO.create(rolePatientVO);

            patientAddSO.setUuid(userUUID);

            return ResponseFactory.ok(patientAddSO);

        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (ServicePreconditionFailedException e) {
            return ResponseFactory.preconditionFailed(e);
        }
    }

    @POST
    @Path("restore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(PatientRestoreSO patientRestoreSO,
                            @Context HttpServletRequest httpServletRequest) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:patient:restore] username: [" + patientRestoreSO.getUsername() + "]");

        try {

            AssertUser.existsNot(
                    patientRestoreSO.getUuid(),
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


            UserLogin.restore(
                    patientRestoreSO.getUuid(),
                    patientRestoreSO.isActive(),
                    patientRestoreSO.getUsername(),
                    patientRestoreSO.getPasswordHash(),
                    patientRestoreSO.getEmail(),
                    patientRestoreSO.getFirstname(),
                    patientRestoreSO.getLastname(),
                    patientRestoreSO.getGender()
            );

            RolePatientVO rolePatientVO = new RolePatientVO(patientRestoreSO.getUuid());
            rolePatientVO.setProgramId(patientRestoreSO.getProgramId());
            rolePatientVO.setTherapistId(patientRestoreSO.getTherapistId());
            RolePatientDAO.create(rolePatientVO);

            return ResponseFactory.ok();

        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (ServicePreconditionFailedException e) {
            return ResponseFactory.preconditionFailed(e);
        }
    }

    @GET
    @Path("get/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
            @PathParam("username") String username,
            @Context HttpServletRequest httpServletRequest
    ) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:patient:get] username: [" + username + "]");

        try {
            UserLoginCompositeVO userLoginCompositeVO = UserLoginCompositeDAO.findByUk_username(username);
            RolePatientVO rolePatientVO = RolePatientDAO.load(userLoginCompositeVO.getUserId());

            PatientRestoreSO patientRestoreSO = new PatientRestoreSO();
            patientRestoreSO.setUuid(userLoginCompositeVO.getUserId());
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

            patientRestoreSO.setProgramId(rolePatientVO.getProgramId());
            patientRestoreSO.setTherapistId(rolePatientVO.getTherapistId());

            return ResponseFactory.ok(patientRestoreSO);

        } catch (EntityNotFoundException e) {
            return ResponseFactory.preconditionFailed("Patient [" + username + "] not found.");
        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @Context HttpServletRequest httpServletRequest
    ) {

        return new LoginPatientGetAllService(httpServletRequest).call();
    }

    @GET
    @Path("delete/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @PathParam("username") String username,
            @Context HttpServletRequest httpServletRequest
    ) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:patient:delete] username: [" + username + "]");

        try {
            UserLoginVO userLoginVO = UserLoginDAO.findByUk_username(username);

            RolePatientDAO.delete(userLoginVO.getUserId());
            UserLoginDAO.delete(userLoginVO.getUserId());
            UserDAO.delete(userLoginVO.getUserId());

            return ResponseFactory.ok();

        } catch (EntityNotFoundException e) {
            return ResponseFactory.preconditionFailed("Patient [" + username + "] not found.");
        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }
    }

}
