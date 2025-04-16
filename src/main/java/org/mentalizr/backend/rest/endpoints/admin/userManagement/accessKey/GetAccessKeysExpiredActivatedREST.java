package org.mentalizr.backend.rest.endpoints.admin.userManagement.accessKey;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.adapter.AccessKeyRestoreSOAdapter;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyPatientCompositeVO;
import org.mentalizr.persistence.rdbms.edao.UserAccessKeyEDAO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyGetExpiredActivatedSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("v1")
public class GetAccessKeysExpiredActivatedREST {

    private static final String SERVICE_ID = "admin/user/accessKey/getExpiredActivated";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExpiredActivated(
            AccessKeyGetExpiredActivatedSO accessKeyGetExpiredActivatedSO,
            @Context HttpServletRequest httpServletRequest
    ) {

        return new Service(httpServletRequest, accessKeyGetExpiredActivatedSO) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected AccessKeyCollectionSO workLoad() throws DataSourceException {

                List<UserAccessKeyPatientCompositeVO> userAccessKeyPatientCompositeVOs
                        = UserAccessKeyEDAO.getLastUsedBefore(accessKeyGetExpiredActivatedSO.getLastUsedBefore());

                List<AccessKeyRestoreSO> accessKeyRestoreSOs
                        = AccessKeyRestoreSOAdapter.from(userAccessKeyPatientCompositeVOs);

                AccessKeyCollectionSO accessKeyCollectionSO = new AccessKeyCollectionSO();
                accessKeyCollectionSO.setCollection(accessKeyRestoreSOs);

                return accessKeyCollectionSO;
            }

        }.call();

    }

}
