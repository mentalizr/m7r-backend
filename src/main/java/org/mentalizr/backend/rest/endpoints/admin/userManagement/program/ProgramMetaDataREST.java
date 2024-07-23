package org.mentalizr.backend.rest.endpoints.admin.userManagement.program;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.exceptions.M7rUnknownEntityException;
import org.mentalizr.backend.programSOCreator.ProgramMetaDataAdapter;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ProgramNotFoundException;
import org.mentalizr.contentManager.fileHierarchy.levels.contentRoot.ProgramConf;
import org.mentalizr.serviceObjects.frontend.program.ProgramMetaDataSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class ProgramMetaDataREST {

    private static final String SERVICE_ID = "/admin/user/program/metaData";

    @GET
    @Path(SERVICE_ID + "/{programId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetaData(
            @PathParam("programId") String programId,
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
            protected ProgramMetaDataSO workLoad() throws M7rUnknownEntityException {
                ContentManager contentManager = ApplicationContext.getContentManager();
                try {
                    ProgramConf programConf = contentManager.getProgramConfig(programId);
                    return ProgramMetaDataAdapter.getProgramMetaDataSO(programId, programConf);
                } catch (ProgramNotFoundException e) {
                    logger.error("Program not found. Cause: " + e.getMessage(), e);
                    throw new M7rUnknownEntityException(e.getMessage(), e);
                }
            }
        }.call();

    }

}
