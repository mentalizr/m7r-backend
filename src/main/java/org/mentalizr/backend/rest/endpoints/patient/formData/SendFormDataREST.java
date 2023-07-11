package org.mentalizr.backend.rest.endpoints.patient.formData;

import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.bson.Document;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.activity.PersistentUserActivity;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.commons.Dates;
import org.mentalizr.persistence.mongo.formData.FormDataConverter;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class SendFormDataREST {

    private static final String SERVICE_ID = "patient/formData/send";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendFormData(FormDataSO formDataSO,
                                 @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return M7rAccessControl.assertValidSessionAsPatientWithId(
                        this.httpServletRequest, formDataSO.getUserId());
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException {
                if (FormDataSOs.isSent(formDataSO)) {
                    logger.error("Inconsistency check failed on calling [sendFormData]: FormData is already sent.");
                    throw new ServicePreconditionFailedException("FormData is already sent.");
                }

                if (!FormDataSOs.isExercise(formDataSO)) {
                    logger.error("Inconsistency check failed on calling [sendFormData]: FormData is no exercise.");
                    throw new ServicePreconditionFailedException("FormData is no exercise.");
                }
            }

            @Override
            protected Object workLoad() {

                ExerciseSO exerciseSO = formDataSO.getExercise();
                exerciseSO.setSent(true);
                exerciseSO.setLastModifiedTimestamp(Dates.currentTimestampAsISO());

                Document document = FormDataConverter.convert(formDataSO);
                FormDataMongoHandler.createOrUpdate(document);

                return null;
            }

            @Override
            protected void updateActivityStatus() {
                PersistentUserActivity.update(this.authorization);
            }

            @Override
            protected void logLeave() {
                String userId = formDataSO.getUserId();
                String contentId = formDataSO.getContentId();
                logger.debug("[" + SERVICE_ID + "][" + userId + "][" + contentId + "] completed.");
            }

        }.call();

    }

}
