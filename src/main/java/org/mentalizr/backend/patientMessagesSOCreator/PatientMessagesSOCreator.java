package org.mentalizr.backend.patientMessagesSOCreator;

import org.mentalizr.commons.Dates;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.persistence.mongo.formData.FormDataTimestampUpdater;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.therapist.patientMessage.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class PatientMessagesSOCreator {

    private final String userIdPatient;
    private final String userIdTherapist;

    private static class PatientMessageSOComparator implements Comparator<PatientMessageSO> {
        @Override
        public int compare(PatientMessageSO pm1, PatientMessageSO pm2) {
            long timestampPm1 = PatientMessageSOs.getTimestamp(pm1);
            long timestampPm2 = PatientMessageSOs.getTimestamp(pm2);
            return Long.compare(timestampPm1, timestampPm2);
        }
    }

    public PatientMessagesSOCreator(String userIdPatient, String userIdTherapist) {
        this.userIdPatient = userIdPatient;
        this.userIdTherapist = userIdTherapist;
    }

    public PatientMessagesSO create() {
        List<FormDataSO> formDataSOExercises = FormDataDAO.getAllExercises(this.userIdPatient);
        FormDataTimestampUpdater.markExerciseAsSeenByTherapist(formDataSOExercises);
        List<PatientMessageSO> patientMessageSOList = new ArrayList<>();
        for (FormDataSO formDataSO : formDataSOExercises) {
            List<PatientMessageSO> patientMessageSOs = createPatientMessageSO(formDataSO);
            patientMessageSOList.addAll(patientMessageSOs);
        }
        patientMessageSOList.sort(new PatientMessageSOComparator());
        return new PatientMessagesSO(this.userIdPatient, patientMessageSOList);
    }

    private List<PatientMessageSO> createPatientMessageSO(FormDataSO formDataSO) {
        List<PatientMessageSO> patientMessageSOs = new ArrayList<>();

        String isoDateExercise = formDataSO.getExercise().getLastModifiedTimestamp();

        PatientMessageExerciseSO patientMessageExerciseSO = new PatientMessageExerciseSO();
        patientMessageExerciseSO.setMessageId(UUID.randomUUID().toString());
        patientMessageExerciseSO.setTimestamp(Dates.toEpochMilli(isoDateExercise));
        patientMessageExerciseSO.setSenderId(this.userIdPatient);
        patientMessageExerciseSO.setExerciseId(formDataSO.getContentId());
        patientMessageExerciseSO.setDate(Dates.asGermanDateTime(isoDateExercise));
        patientMessageExerciseSO.setNew(!formDataSO.getExercise().isSeenByTherapist());
        patientMessageExerciseSO.setHasFeedback(FormDataSOs.hasFeedback(formDataSO));
        PatientMessageSO patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageExercise(patientMessageExerciseSO);
        patientMessageSOs.add(patientMessageSO);

        if (FormDataSOs.hasFeedback(formDataSO)) {
            String isoDateFeedback = formDataSO.getFeedback().getCreatedTimestamp();

            PatientMessageFeedbackSO patientMessageFeedbackSO = new PatientMessageFeedbackSO();
            patientMessageFeedbackSO.setMessageId(UUID.randomUUID().toString());
            patientMessageFeedbackSO.setTimestamp(Dates.toEpochMilli(isoDateFeedback));
            patientMessageFeedbackSO.setSenderId(this.userIdTherapist);
            patientMessageFeedbackSO.setExerciseId(formDataSO.getContentId());
            patientMessageFeedbackSO.setDate(Dates.asGermanDateTime(isoDateFeedback));
            patientMessageFeedbackSO.setNew(false);
            patientMessageFeedbackSO.setText(formDataSO.getFeedback().getText());
            patientMessageFeedbackSO.setReadByReceiver(formDataSO.getFeedback().isSeenByPatient());
            patientMessageSO = new PatientMessageSO();
            patientMessageSO.setPatientMessageFeedback(patientMessageFeedbackSO);
            patientMessageSOs.add(patientMessageSO);
        }

        return patientMessageSOs;
    }

}
