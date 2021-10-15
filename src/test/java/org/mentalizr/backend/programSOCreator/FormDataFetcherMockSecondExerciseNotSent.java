package org.mentalizr.backend.programSOCreator;

import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FeedbackSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

import java.util.ArrayList;

public class FormDataFetcherMockSecondExerciseNotSent implements FormDataFetcher {

    @Override
    public FormDataSO fetch(String userId, String contentId) throws DocumentNotFoundException {
        if (contentId.equals("m1_sm1_s2")) return buildFirst(userId);
        if (contentId.equals("m1_sm2_s2")) return buildSecond(userId);
        throw new DocumentNotFoundException();
    }

    private static FormDataSO buildFirst(String userId) {
        FormDataSO formDataSO = new FormDataSO();
        formDataSO.setUserId(userId);
        formDataSO.setContentId("m1_sm1_s2");

        ExerciseSO exerciseSO = new ExerciseSO();
        exerciseSO.setSent(true);
        formDataSO.setExercise(exerciseSO);

        formDataSO.setFormElementDataList(new ArrayList<>());

        FeedbackSO feedbackSO = new FeedbackSO();
        feedbackSO.setText("text");
        formDataSO.setFeedback(feedbackSO);

        return formDataSO;
    }

    private static FormDataSO buildSecond(String userId) {
        FormDataSO formDataSO = new FormDataSO();
        formDataSO.setUserId(userId);
        formDataSO.setContentId("m1_sm2_s2");

        ExerciseSO exerciseSO = new ExerciseSO();
        exerciseSO.setSent(false);
        formDataSO.setExercise(exerciseSO);

        formDataSO.setFormElementDataList(new ArrayList<>());

        return formDataSO;
    }


}
