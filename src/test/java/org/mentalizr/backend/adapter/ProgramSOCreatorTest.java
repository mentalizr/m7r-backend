package org.mentalizr.backend.adapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.mentalizr.serviceObjects.frontend.program.ProgramSOX;
import org.mentalizr.serviceObjects.frontend.program.StepSO;

import java.util.List;

class ProgramSOCreatorTest {

    @Test
    void firstExerciseNotSent() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockFirstExerciseNotSent();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        Assertions.assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm1_s2"));
    }

    @Test
    void firstExerciseSent() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockFirstExerciseSentNoFeedback();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        Assertions.assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm1_s3"));
    }

    @Test
    void firstExerciseFeedback() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockFirstExerciseSentNoFeedback();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        Assertions.assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm2_s2"));
    }



}