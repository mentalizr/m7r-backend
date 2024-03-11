package org.mentalizr.backend.rest.endpoints.admin.userManagement.patient;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.adapter.PatientRestoreSOAdapter;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreCollectionSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("v1")
public class GetAllPatientsREST {

    private static final String SERVICE_ID = "admin/user/patient/getAll";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context HttpServletRequest httpServletRequest) {

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
            protected PatientRestoreCollectionSO workLoad() throws DataSourceException, EntityNotFoundException {
                List<UserLoginCompositeVO> userLoginCompositeVOs = UserLoginCompositeDAO.findAllPatients();
                PatientRestoreCollectionSO patientRestoreCollectionSO = new PatientRestoreCollectionSO();

                for (UserLoginCompositeVO userLoginCompositeVO : userLoginCompositeVOs) {
                    PatientRestoreSO patientRestoreSO = createPatientRestoreSO(userLoginCompositeVO);
                    patientRestoreCollectionSO.getCollection().add(patientRestoreSO);
                }

                return patientRestoreCollectionSO;
            }

            private PatientRestoreSO createPatientRestoreSO(UserLoginCompositeVO userLoginCompositeVO) throws DataSourceException, EntityNotFoundException {
                String userId = userLoginCompositeVO.getUserId();

                RolePatientVO rolePatientVO = RolePatientDAO.load(userId);
                PatientProgramVO patientProgramVO = PatientProgramDAO.findByUk_user_id(userId);

                PatientRestoreSO patientRestoreSO = PatientRestoreSOAdapter.from(userLoginCompositeVO);

                patientRestoreSO.setProgramId(patientProgramVO.getProgramId());
                patientRestoreSO.setBlocking(patientProgramVO.getBlocking());
                patientRestoreSO.setTherapistId(rolePatientVO.getTherapistId());

                return patientRestoreSO;
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject()));
            }

        }.call();

    }

}
