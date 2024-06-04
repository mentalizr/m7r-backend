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
import org.mentalizr.serviceObjects.requestObjects.ActivityStatRequestSO;
import org.mentalizr.serviceObjects.stateObjects.StatisticResults;
import org.mentalizr.serviceObjects.userManagement.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("v1")
public class StatisticActivityREST {

    private static final String SERVICE_ID = "admin/user/activity/stat";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response statistic(
            ActivityStatRequestSO activityStatRequestSO,
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
            protected Object workLoad() throws DataSourceException {
                ProgramCollectionSO programCollectionSO = getAllPrograms();
                ActivityStatisticCollectionSO activityStatisticCollectionSO = new ActivityStatisticCollectionSO();

                for (ProgramSO programSO: programCollectionSO.getCollection()) {
                    StatisticResults statisticResults =
                            new StatisticResults(getActivityStatusMessageCollectionSO(programSO));

                    activityStatisticCollectionSO.getCollection()
                            .add(new ProgramStatisticSO(programSO.getProgramId(),
                                    statisticResults.getActiveUserCount(),
                                    statisticResults.calAvgInteraction(),
                                    statisticResults.calMinInteraction(),
                                    statisticResults.calMaxInteraction()));
                }
                return activityStatisticCollectionSO;
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

            private ActivityStatusMessageCollectionSO getActivityStatusMessageCollectionSO(ProgramSO programSO)
                    throws DataSourceException {
                List<PatientProgramVO> userIdsOfProgram =
                        PatientProgramDAO.findByFk_program_id(programSO.getProgramId());

                List<String> restIds = new ArrayList<>();
                restIds.add("patient/programContent");
                restIds.add("patient/formData/save");

                return ActivityMessageConverter.convertDocumentListToCollection(
                        ActivityMessageMongoHandler
                                .fetchStatisticData(userIdsOfProgram.stream().map(PatientProgramVO::getUserId).toList(),
                                        restIds,
                                        activityStatRequestSO.getFromTimestamp(),
                                        activityStatRequestSO.getUntilTimestamp()));
            }
        }.call();
    }
}
