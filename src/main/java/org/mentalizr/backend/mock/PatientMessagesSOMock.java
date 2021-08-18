package org.mentalizr.backend.mock;

import org.mentalizr.serviceObjects.frontend.therapist.patientMessage.*;

import java.util.ArrayList;
import java.util.List;

public class PatientMessagesSOMock {

    public static PatientMessagesSO createPatientMessagesSO(String therapistId, String patientId) {

        List<PatientMessageSO> patientMessageSOList = new ArrayList<>();

        PatientMessagePlainSO patientMessagePlainSO = new PatientMessagePlainSO();
        patientMessagePlainSO.setSenderId(patientId);
        patientMessagePlainSO.setDate("2021-08-17");
        patientMessagePlainSO.setNew(false);
        patientMessagePlainSO.setText("This is a message text for a plain message from patient to therapist.");

        PatientMessageSO patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessagePlain(patientMessagePlainSO);
        patientMessageSOList.add(patientMessageSO);

        PatientMessageExerciseSO patientMessageExerciseSO = new PatientMessageExerciseSO();
        patientMessageExerciseSO.setSenderId(patientId);
        patientMessageExerciseSO.setExerciseId("my-exercise-id-1");
        patientMessageExerciseSO.setHasFeedback(true);
        patientMessageExerciseSO.setDate("2021-08-01");
        patientMessageExerciseSO.setNew(false);

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageExercise(patientMessageExerciseSO);
        patientMessageSOList.add(patientMessageSO);

        patientMessageExerciseSO = new PatientMessageExerciseSO();
        patientMessageExerciseSO.setSenderId(patientId);
        patientMessageExerciseSO.setExerciseId("my-exercise-id-2");
        patientMessageExerciseSO.setHasFeedback(false);
        patientMessageExerciseSO.setDate("2021-08-02");
        patientMessageExerciseSO.setNew(true);

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageExercise(patientMessageExerciseSO);
        patientMessageSOList.add(patientMessageSO);

        PatientMessageFeedbackSO patientMessageFeedbackSO = new PatientMessageFeedbackSO();
        patientMessageFeedbackSO.setSenderId(therapistId);
        patientMessageFeedbackSO.setDate("2021-08-03");
        patientMessageFeedbackSO.setText("This is a feedback text for a finished exercise.");
        patientMessageFeedbackSO.setExerciseId("my-exercise-id-1");

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageFeedback(patientMessageFeedbackSO);
        patientMessageSOList.add(patientMessageSO);

        PatientMessagesSO patientMessagesSO = new PatientMessagesSO();
        patientMessagesSO.setPatientId(patientId);
        patientMessagesSO.setPatientMessages(patientMessageSOList);

        return patientMessagesSO;
    }

}
