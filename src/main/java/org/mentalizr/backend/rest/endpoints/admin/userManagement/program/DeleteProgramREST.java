package org.mentalizr.backend.rest.endpoints.admin.userManagement.program;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class DeleteProgramREST {

    private static final String SERVICE_ID = "/admin/user/program/delete";

    @GET
    @Path(SERVICE_ID + "/{programId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @PathParam("programId") String programId,
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
            protected Object workLoad() throws DataSourceException, EntityNotFoundException {
                ProgramDAO.load(programId);
                ProgramDAO.delete(programId);
                return null;
            }

        }.call();

    }

}
