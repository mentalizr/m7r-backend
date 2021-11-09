package org.mentalizr.backend.rest.endpoints.patient;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;

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

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatient;

@Path("v1")
public class MediaAVREST {

    private static final String SERVICE_ID = "mediaAV";

    @GET
    @Path(SERVICE_ID + "/{audioVideo}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response mediaVideo(
            @PathParam("audioVideo") String audioVideo,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() {
                return assertIsLoggedInAsPatient(httpServletRequest);
            }

            @Override
            protected Object workLoad() throws ContentManagerException, IOException {
                PatientProgramVO patientProgramVO = getPatientHttpSessionAttribute().getPatientProgramVO();
                String programId = patientProgramVO.getProgramId();
                ContentManager contentManager = ApplicationContext.getContentManager();
                java.nio.file.Path mediaPath = contentManager.getMediaResource(programId, audioVideo);
                return new FileInputStream(mediaPath.toFile());
            }

        }.call();
    }
}
