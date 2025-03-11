package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.adapter.AccessKeyRestoreSOAdapter;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.serviceObjects.requestObjects.UserListQuerySO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Path("v1")
public class GetAllQueryAccessKeysREST {
    private static final String SERVICE_ID = "admin/user/accessKey/query/getAll";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(UserListQuerySO userListQuerySO,
                           @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected AccessKeyCollectionSO workLoad() throws DataSourceException, EntityNotFoundException {
                List<UserAccessKeyCompositeVO> userAccessKeyCompositeDAOs = new ArrayList<>();
                logger.info("Reached Service: " + SERVICE_ID + " workload.");
                if (!userListQuerySO.getProgramName().isEmpty() && userListQuerySO.getProjectName().isEmpty()) {
                    logger.info("I landed in Program");
                    userAccessKeyCompositeDAOs =
                            UserAccessKeyCompositeDAO.findAllByProgram(userListQuerySO.getProgramName());
                } else if (userListQuerySO.getProgramName().isEmpty() && !userListQuerySO.getProjectName().isEmpty()) {
                    logger.info("I landed in Project");
                    userAccessKeyCompositeDAOs =
                            UserAccessKeyCompositeDAO.findAllByProjectId(userListQuerySO.getProjectName());
                } else if (!userListQuerySO.getProjectName().isEmpty() && !userListQuerySO.getProgramName().isEmpty()) {
                    logger.info("I landed in Program and Project");
                    userAccessKeyCompositeDAOs =
                            UserAccessKeyCompositeDAO.findAllByProgramAndProject(
                                    userListQuerySO.getProgramName(),
                                    userListQuerySO.getProjectName());
                }
                AccessKeyCollectionSO accessKeyCollectionSO = new AccessKeyCollectionSO();

                for (UserAccessKeyCompositeVO userAccessKeyCompositeVO : userAccessKeyCompositeDAOs) {
                    AccessKeyRestoreSO accessKeyRestoreSO = createAccessKeyRestoreSO(userAccessKeyCompositeVO);
                    accessKeyCollectionSO.getCollection().add(accessKeyRestoreSO);
                }

                return accessKeyCollectionSO;
            }

            private AccessKeyRestoreSO createAccessKeyRestoreSO(UserAccessKeyCompositeVO userAccessKeyCompositeVO)
                    throws DataSourceException, EntityNotFoundException {
                String userId = userAccessKeyCompositeVO.getUserId();

                RolePatientVO rolePatientVO = RolePatientDAO.load(userId);
                PatientProgramVO patientProgramVO = PatientProgramDAO.findByUk_user_id(userId);

                AccessKeyRestoreSO accessKeyRestoreSO = AccessKeyRestoreSOAdapter.from(userAccessKeyCompositeVO);

                accessKeyRestoreSO.setProgramId(patientProgramVO.getProgramId());
                accessKeyRestoreSO.setTherapistId(rolePatientVO.getTherapistId());
                accessKeyRestoreSO.setProjectId(rolePatientVO.getProjectId());

                return accessKeyRestoreSO;
            }
        }.call();
    }
}
