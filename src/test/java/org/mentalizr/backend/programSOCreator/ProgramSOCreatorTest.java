package org.mentalizr.backend.programSOCreator;

import org.junit.jupiter.api.Test;
import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.mentalizr.serviceObjects.frontend.program.ProgramSOX;
import org.mentalizr.serviceObjects.frontend.program.StepSO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("ConstantConditions")
class ProgramSOCreatorTest {

    @Test
    void blockingModeFirstExerciseNotSent() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockFirstExerciseNotSent();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm1_s2"));
        assertTrue(programSO.getModules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(0).isAccessible());
        assertFalse(programSO.getModules().get(0).getSubmodules().get(1).isAccessible());
    }

    @Test
    void blockingModeFirstExerciseSentNoFeedback() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockFirstExerciseSentNoFeedback();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm1_s3"));
        assertTrue(programSO.getModules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(0).isAccessible());
        assertFalse(programSO.getModules().get(0).getSubmodules().get(1).isAccessible());
    }

    @Test
    void blockingModeFirstExerciseFeedback() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockFirstExerciseSentFeedback();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm2_s2"));
        assertTrue(programSO.getModules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(1).isAccessible());
    }

    @Test
    void blockingModeSecondExerciseNotSent() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockSecondExerciseNotSent();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm2_s2"));
        assertTrue(programSO.getModules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(1).isAccessible());
    }

    @Test
    void blockingModeSecondExerciseSentNoFeedback() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockSecondExerciseSentNoFeedback();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm2_s3"));
        assertTrue(programSO.getModules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(1).isAccessible());
    }

    @Test
    void blockingModeSecondExerciseSentFeedback() {
        String userId = "dummy";
        boolean blocking = true;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockSecondExerciseSentFeedback();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm2_s4"));
        assertTrue(programSO.getModules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(1).isAccessible());
    }

    @Test
    void nonBlockingMode() {
        String userId = "dummy";
        boolean blocking = false;
        ProgramStructure programStructure = ProgramStructureTestEntities.withFeedback();
        FormDataFetcher formDataFetcher = new FormDataFetcherMockFirstExerciseNotSent();

        ProgramSOCreator programSOCreator = new ProgramSOCreator(userId, blocking, programStructure, formDataFetcher);
        ProgramSO programSO = programSOCreator.create();

        System.out.println(ProgramSOX.toJsonWithFormatting(programSO));

        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);

        assertTrue(ProgramSOListTestHelper.isAccessibleUntil(stepSOList, "m1_sm2_s4"));
        assertTrue(programSO.getModules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(0).isAccessible());
        assertTrue(programSO.getModules().get(0).getSubmodules().get(1).isAccessible());
    }

}