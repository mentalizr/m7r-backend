package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUser;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertUserLogin;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.persistence.rdbms.userAdmin.UserLogin;
import org.mentalizr.serviceObjects.userManagement.TherapistAddSO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreCollectionSO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("v1/admin/user/therapist")
public class EndpointUserManagementTherapist {

    private static final Logger logger = LoggerFactory.getLogger(EndpointUserManagementTherapist.class);

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TherapistAddSO therapistAddSO,
                           @Context HttpServletRequest httpServletRequest) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:therapist:add] username: [" + therapistAddSO.getUsername() + "]");

        logger.debug("therapist title: " + therapistAddSO.getTitle());

        try {
            AssertUserLogin.existsNotWithUsername(
                    therapistAddSO.getUsername(),
                    "User with specified username [%s] is preexisting."
            );

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

            return ResponseFactory.ok(therapistAddSO);

        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (ServicePreconditionFailedException e) {
            logger.warn(e.getMessage());
            return ResponseFactory.preconditionFailed(e);
        }
    }

    @POST
    @Path("restore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(TherapistRestoreSO therapistRestoreSO,
                                    @Context HttpServletRequest httpServletRequest) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:therapist:restore] username: [" + therapistRestoreSO.getUsername() + "]");

        try {
            try {
                AssertUser.existsNot(
                        therapistRestoreSO.getUserId(),
                        "User with specified UUID [%s] is preexisting."
                );
            } catch (ServicePreconditionFailedException e) {
                return ResponseFactory.preconditionFailed(e);
            }

            UserLogin.restore(
                    therapistRestoreSO.getUserId(),
                    therapistRestoreSO.isActive(),
                    therapistRestoreSO.getUsername(),
                    therapistRestoreSO.getPasswordHash(),
                    therapistRestoreSO.getEmail(),
                    therapistRestoreSO.getFirstname(),
                    therapistRestoreSO.getLastname(),
                    therapistRestoreSO.getGender()
            );

            RoleTherapistVO roleTherapistVO = new RoleTherapistVO(therapistRestoreSO.getUserId());
            RoleTherapistDAO.create(roleTherapistVO);

            return ResponseFactory.ok();

        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
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

        logger.info("[userManagement:therapist:get] username: [" + username + "]");

        try {
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

            return ResponseFactory.ok(therapistRestoreSO);

        } catch (EntityNotFoundException e) {
            return ResponseFactory.preconditionFailed("Therapist [" + username + "] not found.");
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

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:therapist:getAll]");

        List<TherapistRestoreSO> collection = new ArrayList<>();

        try {
            List<RoleTherapistVO> roleTherapistVOList = RoleTherapistDAO.findAll();

            for (RoleTherapistVO roleTherapistVO : roleTherapistVOList) {
                UserVO userVO = UserDAO.load(roleTherapistVO.getUserId());
                UserLoginVO userLoginVO = UserLoginDAO.load(roleTherapistVO.getUserId());

                TherapistRestoreSO therapistRestoreSO = new TherapistRestoreSO();
                therapistRestoreSO.setUserId(roleTherapistVO.getUserId());
                therapistRestoreSO.setActive(userVO.getActive());
                Date firstActive = userVO.getFirstActive();
                therapistRestoreSO.setFirstActive(firstActive != null ? firstActive.toString() : null);
                Date lastActive = userVO.getLastActive();
                therapistRestoreSO.setLastActive(lastActive != null ? lastActive.toString() : null);
                therapistRestoreSO.setUsername(userLoginVO.getUsername());
                therapistRestoreSO.setPasswordHash(userLoginVO.getPasswordHash());
                therapistRestoreSO.setEmail(userLoginVO.getEmail());
                therapistRestoreSO.setFirstname(userLoginVO.getFirstName());
                therapistRestoreSO.setLastname(userLoginVO.getLastName());
                therapistRestoreSO.setGender(userLoginVO.getGender());

                therapistRestoreSO.setTitle(roleTherapistVO.getTitle());

                collection.add(therapistRestoreSO);
            }

            TherapistRestoreCollectionSO therapistRestoreCollectionSO = new TherapistRestoreCollectionSO();
            therapistRestoreCollectionSO.setCollection(collection);

            return ResponseFactory.ok(therapistRestoreCollectionSO);

        } catch (DataSourceException | EntityNotFoundException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }
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

        logger.info("[userManagement:therapist:delete] username: [" + username + "]");

        try {
            UserLoginVO userLoginVO = UserLoginDAO.findByUk_username(username);

            RoleTherapistDAO.delete(userLoginVO.getUserId());
            UserLoginDAO.delete(userLoginVO.getUserId());
            UserDAO.delete(userLoginVO.getUserId());

            return ResponseFactory.ok();

        } catch (EntityNotFoundException e) {
            return ResponseFactory.preconditionFailed("Therapist [" + username + "] not found.");
        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }
    }

}
