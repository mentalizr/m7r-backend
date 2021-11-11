package org.mentalizr.backend.rest.endpoints.admin.userManagement.program;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.userAdmin.Program;
import org.mentalizr.serviceObjects.userManagement.ProgramSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class CreateProgramREST {

    private static final String SERVICE_ID = "/admin/user/program/create";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ProgramSO programSO,
                           @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest){

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException, InfrastructureException {
                AssertProgram.notExisting(programSO.getProgramId());
            }

            @Override
            protected Object workLoad() throws DataSourceException {
                Program.add(programSO.getProgramId());
                return null;
            }

        }.call();

    }

}
