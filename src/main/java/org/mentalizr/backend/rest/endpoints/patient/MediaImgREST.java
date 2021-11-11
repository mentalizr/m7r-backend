package org.mentalizr.backend.rest.endpoints.patient;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.auth.UnauthorizedException;
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
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsPatient;

@Path("v1")
public class MediaImgREST {

    // TODO unify
    private static final String SERVICE_ID = "mediaImg";

    // 27.11.2019 Anmerkung zu @Produces("*/*"):
    // Bei Anforderung von img sendet FF seit V65 "image/webp, */*", vorher "*/*"
    // Damit alte FF-Versionen (wie aktuell in AK-Citrixumgebung) keinen HTTP-Fehler 406 sehen
    // auf "*/*" zurück gestellt.
    // vergl.: https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation/List_of_default_Accept_values
    @GET
    @Path(SERVICE_ID + "/{img}")
    @Produces("*/*")     //image/*   //image/jpeg
    public Response mediaImg(
            @PathParam("img") String img,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return assertIsLoggedInAsPatient(this.httpServletRequest);
            }

            @Override
            protected FileInputStream workLoad() throws ContentManagerException, FileNotFoundException {
                PatientProgramVO patientProgramVO = getPatientHttpSessionAttribute().getPatientProgramVO();
                String programId = patientProgramVO.getProgramId();
                ContentManager contentManager = ApplicationContext.getContentManager();
                java.nio.file.Path mediaPath = contentManager.getMediaResource(programId, img);
                return new FileInputStream(mediaPath.toFile());
            }

            @Override
            protected void logLeave() {
                String userId = this.userHttpSessionAttribute.getUserVO().getId();
                this.logger.debug("[" + SERVICE_ID + "][" + userId + "][" + img + "] completed.");
            }

        }.call();

    }

}
