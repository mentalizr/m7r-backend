package org.mentalizr.backend.rest.endpoints.therapist;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.mentalizr.backend.activity.PersistentUserActivity;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.commons.Dates;
import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FeedbackSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.therapist.feedbackSubmission.FeedbackSubmissionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class SubmitFeedbackREST {

    private static final String SERVICE_ID = "therapist/submitFeedback";

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitFeedback(FeedbackSubmissionSO feedbackSubmissionSO,
                                   @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            private FormDataSO formDataSO;

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Therapist.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected void checkPreconditions() throws ServicePreconditionFailedException {
                String userId = feedbackSubmissionSO.getUserId();
                String contentId = feedbackSubmissionSO.getContentId();

                try {
                    this.formDataSO = FormDataDAO.fetch(userId, contentId);
                } catch (DocumentNotFoundException e) {
                    throw new ServicePreconditionFailedException(
                            "Inconsistency check failed: No FormData found for submitted feedback.");
                }

                if (!FormDataSOs.isExercise(this.formDataSO))
                    throw new ServicePreconditionFailedException("Inconsistency check failed: Content is not exercise.");

                if (FormDataSOs.hasFeedback(this.formDataSO))
                    throw new ServicePreconditionFailedException("Inconsistency check failed: Feedback already submitted.");
            }

            @Override
            protected Object workLoad() {
                String therapistId = this.authorization.getUserId();
                FeedbackSO feedbackSO = new FeedbackSO();
                feedbackSO.setText(feedbackSubmissionSO.getFeedback());
                feedbackSO.setCreatedTimestamp(Dates.currentTimestampAsISO());
                feedbackSO.setTherapistId(therapistId);
                feedbackSO.setSeenByPatient(false);
                feedbackSO.setSeenByPatientTimestamp(Dates.epochAsISO());
                this.formDataSO.setFeedback(feedbackSO);

                FormDataDAO.createOrUpdate(this.formDataSO);

                return null;
            }

            @Override
            protected void updateActivityStatus() {
                PersistentUserActivity.update(this.authorization);
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject("SUBMITTED FEEDBACK { userid: "
                                + feedbackSubmissionSO.getUserId()
                                + " contentid: " + feedbackSubmissionSO.getContentId() +" }")));
            }

        }.call();

    }

}
