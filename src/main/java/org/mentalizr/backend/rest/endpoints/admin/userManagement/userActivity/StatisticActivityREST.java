package org.mentalizr.backend.rest.endpoints.admin.userManagement.userActivity;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.bson.Document;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rIllegalServiceInputException;
import org.mentalizr.backend.rest.endpoints.patient.ProgramContentREST;
import org.mentalizr.backend.rest.endpoints.patient.formData.SaveFormDataREST;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProjectDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.ProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.ProjectVO;
import org.mentalizr.persistence.rdbms.edao.PatientProgramEDAO;
import org.mentalizr.persistence.rdbms.edao.RolePatientEDAO;
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
            protected Object workLoad() throws DataSourceException, M7rIllegalServiceInputException {
                List<String> programIds = obtainProgramIds(activityStatRequestSO);
                List<String> projectLabels = obtainProjectLabels(activityStatRequestSO);

                ActivityStatisticCollectionSO activityStatisticCollectionSO = new ActivityStatisticCollectionSO();
                activityStatisticCollectionSO.setProjects(projectLabels);

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

            private List<String> obtainProgramIds(ActivityStatRequestSO activityStatRequestSO)
                    throws DataSourceException, M7rIllegalServiceInputException {

                List<ProgramVO> programVOList = ProgramDAO.findAll();
                List<String> allProgramIds = programVOList.stream()
                        .map(ProgramVO::getId)
                        .toList();

                List<String> selectedProgramIds = new ArrayList<>();
                if (activityStatRequestSO.isProgramsIncludeMode()) {
                    for (String programId : activityStatRequestSO.getPrograms()) {
                        if (allProgramIds.contains(programId)) {
                            selectedProgramIds.add(programId);
                        } else {
                            throw new M7rIllegalServiceInputException("Program not found: [" + programId + "].");
                        }
                    }
                } else {
                    selectedProgramIds.addAll(allProgramIds);
                    for (String programId : activityStatRequestSO.getPrograms()) {
                        if (selectedProgramIds.contains(programId)) {
                            selectedProgramIds.remove(programId);
                        } else {
                            throw new M7rIllegalServiceInputException("Program not found: [" + programId + "].");
                        }
                    }
                }
                return selectedProgramIds;
            }

            private ActivityRecordCollectionSO getActivityStatusMessageCollectionSO(String programId)
                    throws DataSourceException {

//                List<PatientProgramVO> patientProgramVOs =
//                        PatientProgramDAO.findByFk_program_id(programId);
//                List<String> patientProgramUserIds =
//                        patientProgramVOs.stream()
//                                .map(PatientProgramVO::getUserId)
//                                .toList();

                Set<String> patientIds = obtainPatientIds(programId, activityStatRequestSO);

                Set<String> restIds = new HashSet<>();
                restIds.add(ProgramContentREST.SERVICE_ID);
                restIds.add(SaveFormDataREST.SERVICE_ID);

                List<Document> statisticData = ActivityMessageMongoHandler.fetchStatisticData(
                        patientIds,
                        restIds,
                        activityStatRequestSO.getFromTimestamp(),
                        activityStatRequestSO.getUntilTimestamp());

                return ActivityMessageConverter.convertDocumentListToCollection(statisticData);
            }

            private List<String> obtainProjectLabels(ActivityStatRequestSO activityStatRequestSO)
                    throws DataSourceException, M7rIllegalServiceInputException {

                List<String> projectLabels = new ArrayList<>();
                if (activityStatRequestSO.isProjectsIncludeMode()) {
                    for (String projectId : activityStatRequestSO.getProjects()) {
                        ProjectVO projectVO;
                        try {
                            projectVO = ProjectDAO.load(projectId);
                        } catch (EntityNotFoundException e) {
                            throw new M7rIllegalServiceInputException("Project [" + projectId + "] not found.");
                        }
                        projectLabels.add(projectVO.getLabel());
                    }
                } else if (activityStatRequestSO.getProjects().isEmpty()) {
                    projectLabels.add("*");
                } else {
                    List<ProjectVO> projectVOList = ProjectDAO.findAll();
                    List<String> projectIds = new ArrayList<>(projectVOList.stream()
                            .map(ProjectVO::getId)
                            .toList());
                    for (String projectId : activityStatRequestSO.getProjects()) {
                        projectIds.remove(projectId);
                    }
                    for (String projectId : projectIds) {
                        ProjectVO projectVO = null;
                        try {
                            projectVO = ProjectDAO.load(projectId);
                        } catch (EntityNotFoundException e) {
                            throw new M7rIllegalServiceInputException("Project [" + projectId + "] not found.");
                        }
                        projectLabels.add(projectVO.getLabel());
                    }
                }
                Collections.sort(projectLabels);
                return projectLabels;
            }

            private Set<String> obtainPatientIds(String programId, ActivityStatRequestSO activityStatRequestSO)
                    throws DataSourceException {

                Set<String> patientIds = new HashSet<>();
                if (activityStatRequestSO.isProjectsIncludeMode()) {
                    for (String projectId : activityStatRequestSO.getProjects()) {
                        List<String> patientIdsByProgramAndProject
                                = RolePatientEDAO.findAllUserIdsForProgramAndProject(programId, projectId);
                        patientIds.addAll(patientIdsByProgramAndProject);
                    }
                } else {
                    List<String> allPatientIdsForProgram = PatientProgramEDAO.findUserIdsByFk_program_id(programId);
                    patientIds.addAll(allPatientIdsForProgram);
                    for (String projectId : activityStatRequestSO.getProjects()) {
                        List<String> patientIdsByProjectAndProject
                                = RolePatientEDAO.findAllUserIdsForProgramAndProject(programId, projectId);
                        patientIdsByProjectAndProject.forEach(patientIds::remove);
                    }
                }

                return patientIds;
            }

            private List<String> obtainProjectIds(ActivityStatRequestSO activityStatRequestSO) throws DataSourceException, M7rIllegalServiceInputException {

                List<ProjectVO> projectVOList = ProjectDAO.findAll();
                List<String> allProjectIds = projectVOList.stream()
                        .map(ProjectVO::getId)
                        .toList();

                List<String> selectedProjectIds = new ArrayList<>();
                if (activityStatRequestSO.isProjectsIncludeMode()) {
                    for (String projectId : activityStatRequestSO.getProjects()) {
                        if (allProjectIds.contains(projectId)) {
                            selectedProjectIds.add(projectId);
                        } else {
                            throw new M7rIllegalServiceInputException("Project not found: [" + projectId + "].");
                        }
                    }
                } else {
                    selectedProjectIds.addAll(allProjectIds);
                    for (String projectId : activityStatRequestSO.getProjects()) {
                        if (allProjectIds.contains(projectId)) {
                            selectedProjectIds.remove(projectId);
                        } else {
                            throw new M7rIllegalServiceInputException("Project not found: [" + projectId + "].");
                        }
                    }
                }
                return selectedProjectIds;
            }

        }.call();
    }
}
