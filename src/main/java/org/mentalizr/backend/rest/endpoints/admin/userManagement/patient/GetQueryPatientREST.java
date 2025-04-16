package org.mentalizr.backend.rest.endpoints.admin.userManagement.patient;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.adapter.PatientRestoreSOAdapter;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginPatientCompositeVO;
import org.mentalizr.serviceObjects.requestObjects.UserListQuerySO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreCollectionSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

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
public class GetQueryPatientREST {
    private static final String SERVICE_ID = "admin/user/patient/query/get";

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
            protected Object workLoad() throws DataSourceException {
                List<UserLoginPatientCompositeVO> userLoginPatientCompositeVOS =
                        UserLoginPatientCompositeDAO.findAllUserBy(userListQuerySO);

                List<PatientRestoreSO> patientRestoreSOS = PatientRestoreSOAdapter.from(userLoginPatientCompositeVOS);
                PatientRestoreCollectionSO patientRestoreCollectionSO = new PatientRestoreCollectionSO();
                patientRestoreCollectionSO.setCollection(patientRestoreSOS);
                return patientRestoreCollectionSO;
            }
        }.call();
    }
}
