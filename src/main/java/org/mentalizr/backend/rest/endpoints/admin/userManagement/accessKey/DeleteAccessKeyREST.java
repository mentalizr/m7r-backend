package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertAccessKey;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.persistence.rdbms.edao.PolicyConsentEDAO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyDeleteSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class DeleteAccessKeyREST {

    private static final String SERVICE_ID = "admin/user/accessKey/delete";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            AccessKeyDeleteSO accessKeyDeleteSO,
            @Context HttpServletRequest httpServletRequest
    ) {

        return new Service(httpServletRequest, accessKeyDeleteSO){

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, M7rInfrastructureException {
                AssertAccessKey.isExistingWithAccessKey(getAccessKeyDeleteSO().getAccessKey());
            }

            @Override
            protected Object workLoad() throws DataSourceException, EntityNotFoundException {
                String accessKey = accessKeyDeleteSO.getAccessKey();
                UserAccessKeyVO userAccessKeyVO = UserAccessKeyDAO.findByUk_accessKey(accessKey);
                PatientProgramVO patientProgramVO = PatientProgramDAO.findByUk_user_id(userAccessKeyVO.getUserId());

                PatientProgramDAO.delete(patientProgramVO.getPK());
                RolePatientDAO.delete(userAccessKeyVO.getUserId());
                UserAccessKeyDAO.delete(userAccessKeyVO.getUserId());
                PolicyConsentEDAO.deleteAllForUser(userAccessKeyVO.getUserId());
                UserDAO.delete(userAccessKeyVO.getUserId());

                return null;
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject("DELETED ACCESSKEY { accesskey: " + accessKeyDeleteSO.getAccessKey() + " }")));
            }

            private AccessKeyDeleteSO getAccessKeyDeleteSO() {
                return (AccessKeyDeleteSO) this.serviceObjectRequest;
            }

        }.call();

    }

}
