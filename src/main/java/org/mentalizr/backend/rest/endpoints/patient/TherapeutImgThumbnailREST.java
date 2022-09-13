package org.mentalizr.backend.rest.endpoints.patient;

import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.commons.paths.container.TomcatContainerImgBaseTmpDir;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Path("v1")
public class TherapeutImgThumbnailREST {

    private static final String SERVICE_ID = "patient/therapeutImgThumbnail";

    @GET
    @Path(SERVICE_ID)
    @Produces("image/png")
    public Response therapeutImgThumbnail(@Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return M7rAccessControl.assertValidSessionAsPatient(this.httpServletRequest);
            }

            @Override
            protected FileInputStream workLoad() throws RESTException {
                File image = new File(new TomcatContainerImgBaseTmpDir().toAbsolutePathString(), "dummies/DummyAvatar.png");
                try {
                    return new FileInputStream(image);
                } catch (FileNotFoundException e) {
                    String message = "[therapeutImgThumbnail] Image file not found: " + image.getAbsolutePath();
                    logger.error(message);
                    throw new RESTException(message);
                }
            }

        }.call();
    }
}
