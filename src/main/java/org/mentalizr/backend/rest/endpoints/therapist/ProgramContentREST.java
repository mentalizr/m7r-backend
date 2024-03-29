package org.mentalizr.backend.rest.endpoints.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;

@Path("v1")
public class ProgramContentREST {

    private static final String SERVICE_ID = "therapist/programContent";

    @GET
    @Path(SERVICE_ID + "/{contentId}")
    @Produces(MediaType.TEXT_HTML)
    public Response programContent(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Therapist.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected Object workLoad() throws ContentManagerException, IOException {
                ContentManager contentManager = ApplicationContext.getContentManager();
                java.nio.file.Path stepContentFile = contentManager.getContent(contentId);
                return new FileInputStream(stepContentFile.toFile());
            }

            @Override
            protected void logLeave() {
                String userId = this.authorization.getUserId();
                this.logger.debug("[" + SERVICE_ID + "][" + userId + "][" + contentId + "] completed.");
            }

        }.call();

    }

}
