package org.mentalizr.backend.rest.endpoints.admin.userManagement.program;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
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
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);
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

        }.call();

    }

}
