package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.adapter.TherapistRestoreSOAdapter;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
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
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected TherapistRestoreSO workLoad() throws DataSourceException, EntityNotFoundException {
                UserLoginCompositeVO userLoginCompositeVO = UserLoginCompositeDAO.findByUk_username(username);
                RoleTherapistVO roleTherapistVO = RoleTherapistDAO.load(userLoginCompositeVO.getUserId());

                return TherapistRestoreSOAdapter.from(userLoginCompositeVO, roleTherapistVO);
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject("REQUESTED THERAPIST { username: " + username + " }")));
            }

        }.call();

    }

}
