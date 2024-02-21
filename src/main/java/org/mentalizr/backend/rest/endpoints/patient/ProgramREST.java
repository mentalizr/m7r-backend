package org.mentalizr.backend.rest.endpoints.patient;

import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.programSOCreator.FormDataFetcher;
import org.mentalizr.backend.programSOCreator.FormDataFetcherMongo;
import org.mentalizr.backend.programSOCreator.ProgramSOCreator;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.fileHierarchy.exceptions.ProgramNotFoundException;
import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class ProgramREST {

    private static final String SERVICE_ID = "patient/program";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response program(@Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return M7rAccessControl.assertValidSessionAsPatientAbstract(this.httpServletRequest);
            }

            @Override
            protected ProgramSO workLoad() throws RESTException {
                M7rAuthorization m7rAuthorization = new M7rAuthorization(this.authorization);
                PatientProgramVO patientProgramVO = m7rAuthorization.getUserAsPatientAbstract().getPatientProgramVO();
                ProgramStructure programStructure = obtainProgramStructure(patientProgramVO.getProgramId());
                FormDataFetcher formDataFetcher = new FormDataFetcherMongo();
                ProgramSOCreator programSOCreator = new ProgramSOCreator(
                        patientProgramVO.getUserId(),
                        patientProgramVO.getBlocking(),
                        programStructure,
                        formDataFetcher
                );
                return programSOCreator.create();
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject()));
            }

        }.call();

    }

    private ProgramStructure obtainProgramStructure(String programId) throws RESTException {
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            return contentManager.getProgramStructure(programId);
        } catch (ProgramNotFoundException e) {
            logger.error("Program not found. Cause: " + e.getMessage(), e);
            throw new RESTException(e.getMessage(), e);
        }
    }

}
