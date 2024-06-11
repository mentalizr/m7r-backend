package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.bson.Document;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.endpoints.patient.ProgramContentREST;
import org.mentalizr.backend.rest.endpoints.patient.formData.SaveFormDataREST;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.ProgramVO;
import org.mentalizr.serviceObjects.requestObjects.ActivityStatRequestSO;
import org.mentalizr.backend.utils.ActivityStatisticsResult;
import org.mentalizr.serviceObjects.userManagement.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

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
                List<String> programIds = getAllProgramIds();
                ActivityStatisticCollectionSO activityStatisticCollectionSO = new ActivityStatisticCollectionSO();

                for (String programId : programIds) {
                    ActivityRecordCollectionSO activityRecordCollectionSO =
                            getActivityStatusMessageCollectionSO(programId);
                    ActivityStatisticsResult activityStatisticsResult =
                            new ActivityStatisticsResult(programId, activityRecordCollectionSO);
                    ProgramStatisticSO programStatisticSO = activityStatisticsResult.getProgramStatisticSO();
                    activityStatisticCollectionSO.getCollection().add(programStatisticSO);
                }
                return activityStatisticCollectionSO;
            }

            private List<String> getAllProgramIds() throws DataSourceException {
                List<ProgramVO> programVOList = ProgramDAO.findAll();
                return programVOList.stream().map(ProgramVO::getId).toList();
            }

            private ActivityRecordCollectionSO getActivityStatusMessageCollectionSO(String programId)
                    throws DataSourceException {

                List<PatientProgramVO> patientProgramVOs =
                        PatientProgramDAO.findByFk_program_id(programId);
                List<String> patientProgramUserIds =
                        patientProgramVOs.stream().map(PatientProgramVO::getUserId).toList();

                List<String> restIds = new ArrayList<>();
                restIds.add(ProgramContentREST.SERVICE_ID);
                restIds.add(SaveFormDataREST.SERVICE_ID);

                List<Document> statisticData = ActivityMessageMongoHandler.fetchStatisticData(
                        patientProgramUserIds,
                        restIds,
                        activityStatRequestSO.getFromTimestamp(),
                        activityStatRequestSO.getUntilTimestamp());

                return ActivityMessageConverter.convertDocumentListToCollection(statisticData);
            }
        }.call();
    }
}
