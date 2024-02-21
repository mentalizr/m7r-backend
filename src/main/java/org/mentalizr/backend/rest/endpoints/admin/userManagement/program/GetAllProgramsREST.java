package org.mentalizr.backend.rest.endpoints.admin.userManagement.program;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.ProgramVO;
import org.mentalizr.serviceObjects.userManagement.ProgramCollectionSO;
import org.mentalizr.serviceObjects.userManagement.ProgramSO;

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
public class GetAllProgramsREST {

    private static final String SERVICE_ID = "/admin/user/program/getAll";

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
            protected ProgramCollectionSO workLoad() throws DataSourceException {
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

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject()));
            }

        }.call();

    }

}
