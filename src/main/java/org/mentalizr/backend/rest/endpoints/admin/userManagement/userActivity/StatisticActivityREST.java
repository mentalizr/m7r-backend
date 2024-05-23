package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.adapter.PatientRestoreSOAdapter;
import org.mentalizr.backend.rest.endpoints.patient.ProgramContentREST;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.ProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.serviceObjects.userManagement.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("v1")
public class StatisticActivityREST {

    private static final String SERVICE_ID = "admin/user/activity/stat";

    @GET
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response statistic(@Context HttpServletRequest httpServletRequest) {

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
            protected Object workLoad() throws DataSourceException {
                ProgramCollectionSO programCollectionSO = getAllPrograms();
                PatientRestoreCollectionSO patientCollectionSO = getAllPatient();
                ActivityStatisticCollectionSO activityStatisticCollectionSO = new ActivityStatisticCollectionSO();

                programCollectionSO.getCollection().forEach(programSO -> {
                    List<String> userIdsOfProgram =
                            patientCollectionSO.getCollection().stream()
                                    .filter(patientSO -> patientSO.getProgramId().equals(programSO.getProgramId()))
                                    .map(patientRestoreSO -> patientRestoreSO.getUserId())
                                    .toList();

                    List<String> restIds = new ArrayList<>();
                    restIds.add("patient/programContent");
                    restIds.add("patient/formData/save");

                    Long fromTimeStamp = 0L;
                    Long toTimeStamp = System.currentTimeMillis();

                    ActivityStatusMessageCollectionSO messageCollectionSO = ActivityMessageConverter.convertDocumentListToCollection(
                            ActivityMessageMongoHandler.fetchStatisticData(userIdsOfProgram, restIds, fromTimeStamp, toTimeStamp));

                    activityStatisticCollectionSO.getCollection().add(new ProgramStatisticSO(programSO.getProgramId(), countActiveUser(messageCollectionSO), 0, 0, 0));
                });

                return activityStatisticCollectionSO;
            }

            private PatientRestoreCollectionSO getAllPatient() {
                List<UserLoginCompositeVO> userLoginCompositeVOs = null;
                try {
                    userLoginCompositeVOs = UserLoginCompositeDAO.findAllPatients();

                    PatientRestoreCollectionSO patientRestoreCollectionSO = new PatientRestoreCollectionSO();

                    for (UserLoginCompositeVO userLoginCompositeVO : userLoginCompositeVOs) {
                        PatientRestoreSO patientRestoreSO = createPatientRestoreSO(userLoginCompositeVO);
                        patientRestoreCollectionSO.getCollection().add(patientRestoreSO);
                    }
                    return patientRestoreCollectionSO;
                } catch (DataSourceException | EntityNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            private ProgramCollectionSO getAllPrograms() {
                List<ProgramVO> programVOList = null;
                try {
                    programVOList = ProgramDAO.findAll();

                    List<ProgramSO> collection = new ArrayList<>();
                    for (ProgramVO programVO : programVOList) {
                        ProgramSO programSO = new ProgramSO();
                        programSO.setProgramId(programVO.getId());
                        collection.add(programSO);
                    }

                    ProgramCollectionSO programCollectionSO = new ProgramCollectionSO();
                    programCollectionSO.setCollection(collection);

                    return programCollectionSO;
                } catch (DataSourceException e) {
                    throw new RuntimeException(e);
                }
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

            private int countActiveUser(ActivityStatusMessageCollectionSO messageCollectionSO) {
                return messageCollectionSO.getCollection().stream().map(ActivityMessageSO::getUserId).collect(Collectors.toSet()).size();
            }
        }.call();
    }
}
