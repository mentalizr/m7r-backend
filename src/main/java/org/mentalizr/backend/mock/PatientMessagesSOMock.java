package org.mentalizr.backend.mock;

import org.mentalizr.serviceObjects.frontend.therapist.patientMessage.*;

import java.util.ArrayList;
import java.util.List;

public class PatientMessagesSOMock {

    public static PatientMessagesSO createPatientMessagesSO(String therapistId, String patientId) {

        List<PatientMessageSO> patientMessageSOList = new ArrayList<>();

        PatientMessagePlainSO patientMessagePlainSO = new PatientMessagePlainSO();
        patientMessagePlainSO.setSenderId(patientId);
        patientMessagePlainSO.setDate("17.08.2021 18:17");
        patientMessagePlainSO.setNew(false);
        patientMessagePlainSO.setReadByReceiver(false);
        patientMessagePlainSO.setText("This is a message text for a plain message from patient to therapist.");

        PatientMessageSO patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessagePlain(patientMessagePlainSO);
        patientMessageSOList.add(patientMessageSO);

        patientMessagePlainSO = new PatientMessagePlainSO();
        patientMessagePlainSO.setSenderId(therapistId);
        patientMessagePlainSO.setDate("18.08.2021 11:23");
        patientMessagePlainSO.setNew(false);
        patientMessagePlainSO.setReadByReceiver(true);
        patientMessagePlainSO.setText("This is a plain message from therapist to patient that was read by receiver.");

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessagePlain(patientMessagePlainSO);
        patientMessageSOList.add(patientMessageSO);

        patientMessagePlainSO = new PatientMessagePlainSO();
        patientMessagePlainSO.setSenderId(therapistId);
        patientMessagePlainSO.setDate("18.08.2021 12:45");
        patientMessagePlainSO.setNew(false);
        patientMessagePlainSO.setReadByReceiver(false);
        patientMessagePlainSO.setText("This is a plain message from therapist to patient that was not yet read by receiver.");

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessagePlain(patientMessagePlainSO);
        patientMessageSOList.add(patientMessageSO);


        PatientMessageExerciseSO patientMessageExerciseSO = new PatientMessageExerciseSO();
        patientMessageExerciseSO.setSenderId(patientId);
        patientMessageExerciseSO.setExerciseId("my-exercise-id-1");
        patientMessageExerciseSO.setHasFeedback(true);
        patientMessageExerciseSO.setDate("01.08.2021 09:17");
        patientMessageExerciseSO.setNew(false);

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageExercise(patientMessageExerciseSO);
        patientMessageSOList.add(patientMessageSO);

        patientMessageExerciseSO = new PatientMessageExerciseSO();
        patientMessageExerciseSO.setSenderId(patientId);
        patientMessageExerciseSO.setExerciseId("my-exercise-id-2");
        patientMessageExerciseSO.setHasFeedback(false);
        patientMessageExerciseSO.setDate("02.08.2021 11:44");
        patientMessageExerciseSO.setNew(true);

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageExercise(patientMessageExerciseSO);
        patientMessageSOList.add(patientMessageSO);

        PatientMessageFeedbackSO patientMessageFeedbackSO = new PatientMessageFeedbackSO();
        patientMessageFeedbackSO.setSenderId(therapistId);
        patientMessageFeedbackSO.setDate("2021-08-03 13:57");
        patientMessageFeedbackSO.setText("This is a feedback text for a finished exercise. Not yet read.");
        patientMessageFeedbackSO.setReadByReceiver(false);
        patientMessageFeedbackSO.setExerciseId("my-exercise-id-1");

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageFeedback(patientMessageFeedbackSO);
        patientMessageSOList.add(patientMessageSO);

        patientMessageFeedbackSO = new PatientMessageFeedbackSO();
        patientMessageFeedbackSO.setSenderId(therapistId);
        patientMessageFeedbackSO.setDate("2021-08-04 14:21");
        patientMessageFeedbackSO.setText("This is a feedback text for a finished exercise. Already read.");
        patientMessageFeedbackSO.setReadByReceiver(true);
        patientMessageFeedbackSO.setExerciseId("my-exercise-id-2");

        patientMessageSO = new PatientMessageSO();
        patientMessageSO.setPatientMessageFeedback(patientMessageFeedbackSO);
        patientMessageSOList.add(patientMessageSO);


        PatientMessagesSO patientMessagesSO = new PatientMessagesSO();
        patientMessagesSO.setPatientId(patientId);
        patientMessagesSO.setPatientMessages(patientMessageSOList);

        return patientMessagesSO;
    }

}
