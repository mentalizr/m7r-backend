package org.mentalizr.backend.rest.endpoints.admin.patientStatus;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.bson.Document;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.DocumentPreexistingException;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusConverter;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusMongoHandler;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class RestorePatientStatusREST {

    private static final String SERVICE_ID = "admin/patientStatus/restore";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(PatientStatusSO patientStatusSO,
                            @Context HttpServletRequest httpServletRequest) {

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
            protected Object workLoad() throws RESTException {
                Document document = PatientStatusConverter.convert(patientStatusSO);
                try {
                    PatientStatusMongoHandler.restore(document);
                } catch (DocumentPreexistingException e) {
                    throw new RESTException("REST method [" + SERVICE_ID + "]. PatientStatus cannot be restored as it is preexisting.");
                }
                return null;
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject("userid: " + patientStatusSO.getUserId())));
            }

        }.call();

    }

}
