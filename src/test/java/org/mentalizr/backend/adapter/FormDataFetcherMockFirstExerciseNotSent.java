package org.mentalizr.backend.adapter;

import org.mentalizr.serviceObjects.frontend.patient.formData.ExerciseSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

import java.util.ArrayList;

public class FormDataFetcherMockFirstExerciseNotSent implements FormDataFetcher {

    @Override
    public FormDataSO fetch(String userId, String contentId) {
        if (contentId.equals("m1_sm1_s2")) return build(userId);
        return null;
    }

    private static FormDataSO build(String userId) {
        FormDataSO formDataSO = new FormDataSO();
        formDataSO.setUserId(userId);
        formDataSO.setContentId("m1_sm1_s2");

        ExerciseSO exerciseSO = new ExerciseSO();
        exerciseSO.setSent(false);
        formDataSO.setExerciseSO(exerciseSO);

        formDataSO.setFormElementDataList(new ArrayList<>());

        return formDataSO;
    }

}
