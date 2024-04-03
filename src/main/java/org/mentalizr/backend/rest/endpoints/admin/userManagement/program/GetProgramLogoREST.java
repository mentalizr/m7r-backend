package org.mentalizr.backend.rest.endpoints.admin.userManagement.program;

import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.exceptions.M7rNoSuchResourceException;
import org.mentalizr.backend.exceptions.M7rUnknownEntityException;
import org.mentalizr.backend.programSOCreator.ProgramMetaDataAdapter;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.commons.paths.container.TomcatContainerImgBaseTmpDir;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ProgramNotFoundException;
import org.mentalizr.contentManager.fileHierarchy.levels.contentRoot.ProgramConf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Path("v1")
public class GetProgramLogoREST {

    private static final String SERVICE_ID = "/admin/user/program/getLogo";

    @GET
    @Path(SERVICE_ID + "/{programId}")
    @Produces("image/png")
    public Response getProgramLogo(
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
            protected FileInputStream workLoad() throws RESTException, M7rNoSuchResourceException, M7rUnknownEntityException {
                ContentManager contentManager = ApplicationContext.getContentManager();
                try {
                    ProgramConf programConf = contentManager.getProgramConfig(programId);
                    if (Strings.isUnspecified(programConf.getLogo()))
                        throw new M7rNoSuchResourceException("Program [" + programId + "] has no logo.");
                    File logo = contentManager.getMediaResource(programId, programConf.getLogo()).toFile();
                    return new FileInputStream(logo);
                } catch (ContentManagerException e) {
                    logger.error("Program not found. Cause: " + e.getMessage(), e);
                    throw new M7rUnknownEntityException(e.getMessage(), e);
                } catch (FileNotFoundException e) {
                    throw new RESTException(e);
                }
            }

        }.call();
    }
}
