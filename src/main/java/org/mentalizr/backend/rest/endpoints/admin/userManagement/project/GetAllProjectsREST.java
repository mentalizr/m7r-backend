package org.mentalizr.backend.rest.endpoints.admin.userManagement.project;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProjectDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.ProjectVO;
import org.mentalizr.serviceObjects.userManagement.ProjectCollectionSO;
import org.mentalizr.serviceObjects.userManagement.ProjectSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("v1")
public class GetAllProjectsREST {

    private static final String SERVICE_ID = "/admin/user/project/getAll";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
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
            protected ProjectCollectionSO workLoad() throws DataSourceException {
                List<ProjectVO> projectVOList = ProjectDAO.findAll();

                List<ProjectSO> collection = new ArrayList<>();
                for (ProjectVO projectVO : projectVOList) {
                    ProjectSO projectSO = new ProjectSO();
                    projectSO.setProjectId(projectVO.getId());
                    projectSO.setLabel(projectVO.getLabel());
                    collection.add(projectSO);
                }

                ProjectCollectionSO projectCollectionSO = new ProjectCollectionSO();
                projectCollectionSO.setCollection(collection);

                return projectCollectionSO;
            }

        }.call();

    }

}
