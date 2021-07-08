package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.ProgramVO;
import org.mentalizr.persistence.rdbms.userAdmin.Program;
import org.mentalizr.serviceObjects.userManagement.ProgramCollectionSO;
import org.mentalizr.serviceObjects.userManagement.ProgramSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("v1/admin/user/program")
public class EndpointUserManagementProgram {

    private static final Logger logger = LoggerFactory.getLogger(EndpointUserManagementProgram.class);

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ProgramSO programSO,
                        @Context HttpServletRequest httpServletRequest) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:program:add] program: [" + programSO.getProgramId() + "]");

        try {
            try {
                AssertProgram.notExisting(programSO.getProgramId());
            } catch (ServicePreconditionFailedException e) {
                return ResponseFactory.preconditionFailed(e);
            }

            Program.add(programSO.getProgramId());

            return ResponseFactory.ok();

        } catch (DataSourceException e) {
            return ResponseFactory.internalServerError(e);
        }
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @Context HttpServletRequest httpServletRequest
    ) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:program:getAll]");

        List<ProgramSO> collection = new ArrayList<>();

        try {
            List<ProgramVO> programVOList = ProgramDAO.findAll();

            for (ProgramVO programVO : programVOList) {
                ProgramSO programSO = new ProgramSO();
                programSO.setProgramId(programVO.getProgramId());

                collection.add(programSO);
            }

            ProgramCollectionSO programCollectionSO = new ProgramCollectionSO();
            programCollectionSO.setCollection(collection);

            return ResponseFactory.ok(programCollectionSO);

        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }
    }

    @GET
    @Path("delete/{programId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @PathParam("programId") String programId,
            @Context HttpServletRequest httpServletRequest
    ) {

        AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);

        logger.info("[userManagement:program:delete] program: [" + programId + "]");

        try {
            ProgramDAO.load(programId);
            ProgramDAO.delete(programId);

            return ResponseFactory.ok();

        } catch (EntityNotFoundException e) {
            return ResponseFactory.preconditionFailed("Program [" + programId + "] not found.");
        } catch (DataSourceException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }
    }

}
