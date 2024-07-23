package org.mentalizr.backend.rest.endpoints.admin.userManagement.project;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProject;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRolePatient;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.userAdmin.Project;
import org.mentalizr.serviceObjects.userManagement.ProjectAssignSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class AssignProjectREST {

    private static final String SERVICE_ID = "/admin/user/project/assign";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ProjectAssignSO projectAssignSO,
                           @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest){

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, M7rInfrastructureException {
                AssertProject.exists(projectAssignSO.getProjectId());
                AssertRolePatient.existsAsNotAssigned(projectAssignSO.getUserId());
            }

            @Override
            protected Object workLoad() throws DataSourceException {
                try {
                    RolePatientVO rolePatientVO = RolePatientDAO.load(projectAssignSO.getUserId());
                    rolePatientVO.setProjectId(projectAssignSO.getProjectId());
                    RolePatientDAO.update(rolePatientVO);
                    return null;
                } catch (EntityNotFoundException e) {
                    throw new RuntimeException("RolePatient-Entity not found. But was checked earlier.", e);
                }
            }

        }.call();

    }

}
