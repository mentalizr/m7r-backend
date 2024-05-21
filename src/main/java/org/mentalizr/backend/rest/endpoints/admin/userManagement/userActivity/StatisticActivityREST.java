package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.adapter.PatientRestoreSOAdapter;
import org.mentalizr.backend.exceptions.*;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.mongo.DocumentNotFoundException;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatisticActivityREST {

    private static final String SERVICE_ID = "admin/user/activity/stat";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response statistic(@Context HttpServletRequest httpServletRequest) {
        return new Service(httpServletRequest) {
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected Object workLoad() throws RESTException, ContentManagerException, M7rInfrastructureException, IOException, DataSourceException, EntityNotFoundException, M7rIllegalServiceInputException, M7rUnknownEntityException, M7rBusinessConstraintException, M7rNoSuchResourceException, M7rBusinessConstraintException, DocumentNotFoundException {
                ProgramCollectionSO programCollectionSO = getAllPrograms();
                PatientRestoreCollectionSO patientRestoreCollectionSO = getAllPatients();
                ActivityStatisticCollectionSO activityStatisticCollectionSO = new ActivityStatisticCollectionSO();

                programCollectionSO.getCollection().forEach(programSO -> {
                    ProgramStatisticSO programStatistic = new ProgramStatisticSO();
                    programStatistic.setProgramName(programSO.getProgramId());
                    programStatistic.setUser(
                            ((int) patientRestoreCollectionSO.getCollection().stream()
                                    .filter(patientRestoreSO -> patientRestoreSO.getProgramId().equals(programSO.getProgramId()))
                                    .count()));

                    programStatistic.setInteractionAvg(0);
                    programStatistic.setInteractionMax(0);
                    programStatistic.setInteractionMin(0);

                    activityStatisticCollectionSO.getCollection().add(programStatistic);
                });

                return activityStatisticCollectionSO;
            }

            @Override
            protected void updateActivityStatus() {}

            protected ProgramCollectionSO getAllPrograms() throws DataSourceException {
                List<ProgramVO> programVOList = ProgramDAO.findAll();

                List<ProgramSO> collection = new ArrayList<>();
                for (ProgramVO programVO : programVOList) {
                    ProgramSO programSO = new ProgramSO();
                    programSO.setProgramId(programVO.getId());

                    collection.add(programSO);
                }

                ProgramCollectionSO programCollectionSO = new ProgramCollectionSO();
                programCollectionSO.setCollection(collection);

                return programCollectionSO;
            }

            protected PatientRestoreCollectionSO getAllPatients()
                    throws DataSourceException, EntityNotFoundException {
                List<UserLoginCompositeVO> userLoginCompositeVOs = UserLoginCompositeDAO.findAllPatients();
                PatientRestoreCollectionSO patientRestoreCollectionSO = new PatientRestoreCollectionSO();

                for (UserLoginCompositeVO userLoginCompositeVO : userLoginCompositeVOs) {
                    PatientRestoreSO patientRestoreSO = createPatientRestoreSO(userLoginCompositeVO);
                    patientRestoreCollectionSO.getCollection().add(patientRestoreSO);
                }
                return patientRestoreCollectionSO;
            }

            protected PatientRestoreSO createPatientRestoreSO(UserLoginCompositeVO userLoginCompositeVO)
                    throws DataSourceException, EntityNotFoundException {
                String userId = userLoginCompositeVO.getUserId();

                RolePatientVO rolePatientVO = RolePatientDAO.load(userId);
                PatientProgramVO patientProgramVO = PatientProgramDAO.findByUk_user_id(userId);

                PatientRestoreSO patientRestoreSO = PatientRestoreSOAdapter.from(userLoginCompositeVO);

                patientRestoreSO.setProgramId(patientProgramVO.getProgramId());
                patientRestoreSO.setBlocking(patientProgramVO.getBlocking());
                patientRestoreSO.setTherapistId(rolePatientVO.getTherapistId());

                return patientRestoreSO;
            }
        }.call();
    }
}
