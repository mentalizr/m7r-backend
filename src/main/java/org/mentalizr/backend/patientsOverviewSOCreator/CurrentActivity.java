package org.mentalizr.backend.patientsOverviewSOCreator;

import org.mentalizr.commons.Dates;
import org.mentalizr.commons.StringHelper;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;

import java.util.Optional;

public class CurrentActivity {

    private final FormDataSO lastActiveFormDataSO;

    public CurrentActivity(String userId) {
        this.lastActiveFormDataSO = getLastActiveFormDataSO(userId);
    }

    private FormDataSO getLastActiveFormDataSO(String userId) {
        Optional<FormDataSO> lastExercise = FormDataDAO.getLastExercise(userId);
        Optional<FormDataSO> lastFeedback = FormDataDAO.getLastFeedback(userId);

        if (lastExercise.isEmpty() && lastFeedback.isEmpty()) return null;
        if (lastExercise.isEmpty()) return lastFeedback.get();
        if (lastFeedback.isEmpty()) return lastExercise.get();

        String timestampExercise = lastExercise.get().getExercise().getLastModifiedTimestamp();
        String timestampFeedback = lastFeedback.get().getFeedback().getCreatedTimestamp();

        return Dates.isYoungerThan(timestampExercise, timestampFeedback) ? lastExercise.get() : lastFeedback.get();
    }

    public String getLastActiveAsGermanDate() {
        if (this.lastActiveFormDataSO == null) return "";
        String isoDate;
        if (FormDataSOs.hasFeedback(this.lastActiveFormDataSO)) {
            isoDate = this.lastActiveFormDataSO.getFeedback().getSeenByPatientTimestamp();
        } else if (FormDataSOs.isExercise(this.lastActiveFormDataSO)) {
            isoDate = this.lastActiveFormDataSO.getExercise().getLastModifiedTimestamp();
        } else {
            throw createInconsistencyException();
        }
        return Dates.asGermanDate(isoDate);
    }

    public String getOverviewMessage() {
        if (this.lastActiveFormDataSO == null) return "";
        if (FormDataSOs.hasFeedback(this.lastActiveFormDataSO)) {
            String feedback = this.lastActiveFormDataSO.getFeedback().getText();
            return StringHelper.limit(feedback, 30);
        } else if (FormDataSOs.isExercise(this.lastActiveFormDataSO)) {
            return "Übung abgeschlossen.";
        } else {
            throw createInconsistencyException();
        }
    }

    public boolean hasUpdate() {
        if (notHasLastActiveFormData()) return false;
        if (hasFeedback()) return false;
        if (hasExercise()) return !this.lastActiveFormDataSO.getExercise().isSeenByTherapist();
        throw createInconsistencyException();
    }

    public boolean hasReceiveStatus() {
        if (notHasLastActiveFormData()) return false;
        if (hasFeedback()) return true;
        if (hasExercise()) return false;
        throw createInconsistencyException();
    }

    public boolean isReceived() {
        if (notHasLastActiveFormData()) return false;
        if (hasFeedback()) return this.lastActiveFormDataSO.getFeedback().isSeenByPatient();
        if (hasExercise()) return false;
        throw createInconsistencyException();
    }

    private boolean notHasLastActiveFormData() {
        return this.lastActiveFormDataSO == null;
    }

    private boolean hasExercise() {
        return FormDataSOs.isExercise(this.lastActiveFormDataSO);
    }

    private boolean hasFeedback() {
        return FormDataSOs.hasFeedback(this.lastActiveFormDataSO);
    }

    private IllegalStateException createInconsistencyException() {
        return new IllegalStateException("Inconsistency: " +
                "current FormData must exactly be one of feedback or exercise. " +
                "userId: [" + this.lastActiveFormDataSO.getUserId() + "]. " +
                "contentId: [" + this.lastActiveFormDataSO.getContentId() + "].");
    }

}
