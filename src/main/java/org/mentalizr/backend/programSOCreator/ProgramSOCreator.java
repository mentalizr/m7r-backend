package org.mentalizr.backend.programSOCreator;

import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSOs;
import org.mentalizr.serviceObjects.frontend.program.*;

import java.util.List;

public class ProgramSOCreator {

    private final String userId;
    private final boolean blocking;
    private final ProgramStructure programStructure;
    private final FormDataFetcher formDataFetcher;

    public ProgramSOCreator(String userId, boolean blocking, ProgramStructure programStructure, FormDataFetcher formDataFetcher) {
        this.userId = userId;
        this.blocking = blocking;
        this.programStructure = programStructure;
        this.formDataFetcher = formDataFetcher;
    }

    public ProgramSO create() {

        ProgramSO programSO = ProgramAdapter.getProgramSO(programStructure);

        programSO.setBlocking(this.blocking);
        setAccessibilityStatusForSteps(programSO);
        setAccessibilityStatusForModulesAndSubmodules(programSO);

        return programSO;
    }

//    private ProgramStructure getProgramStructure(String programId) throws ProgramSOBuilderException {
//        ContentManager contentManager = ApplicationContext.getContentManager();
//        try {
//            return contentManager.getProgramStructure(programId);
//        } catch (ProgramNotFoundException e) {
//            throw new ProgramSOBuilderException(e);
//        }
//    }

    private void setAccessibilityStatusForSteps(ProgramSO programSO) {
        List<StepSO> stepSOList = ProgramAdapterUtils.buildStepSOList(programSO);
        if (programSO.isBlocking()) {
            setAccessibilityForBlockingMode(stepSOList);
        } else {
            setAllStepsAsAccessible(stepSOList);
        }
    }

    private void setAccessibilityForBlockingMode(List<StepSO> stepSOList) {
        boolean accessibility = true;
        StepSOIterator stepSOIterator = new StepSOIterator(stepSOList);
        while (stepSOIterator.hasNext()) {
            StepSO currentStepSO = stepSOIterator.getNext();

            if (accessibility) {
                if (currentStepSO.isExercise() && !isExerciseSent(currentStepSO)) {
                    accessibility = false;
                } else if (currentStepSO.isFeedback() && isFeedbackPending(stepSOIterator)) {
                    accessibility = false;
                }
                currentStepSO.setAccessible(true);
            } else {
                currentStepSO.setAccessible(false);
            }
        }
    }

    private boolean isFeedbackPending(StepSOIterator stepSOIterator) {

        if (!stepSOIterator.hasPrevious())
            throw new RuntimeException("Inconsistency exception. Previous step expected for feedback.");
        StepSO stepSOExercise = stepSOIterator.getStepSOList().get(stepSOIterator.getIndex() - 1);

        if (!stepSOExercise.isExercise())
            throw new RuntimeException("Inconsistency exception. Preceding step of feedback is expected to be an exercise.");

        FormDataSO formDataSO;
        try {
            formDataSO = this.formDataFetcher.fetch(this.userId, stepSOExercise.getId());
        } catch (DocumentNotFoundException e) {
            throw new RuntimeException("Inconsistency exception: Sent exercise expected as a preceding page of feedback.");
        }
        if (!FormDataSOs.isExercise(formDataSO))
            throw new RuntimeException("Inconsistency exception: FormDataSO with exercise expected for exercise page.");
        if (!FormDataSOs.isSent(formDataSO))
            throw new RuntimeException("Inconsistency exception: FormDataSO with sent exercise expected.");
        return !FormDataSOs.hasFeedback(formDataSO);
    }

    private void setAllStepsAsAccessible(List<StepSO> stepSOList) {
        stepSOList.forEach(stepSO -> stepSO.setAccessible(true));
    }

    private boolean isExerciseSent(StepSO stepSOExercise) {
        FormDataSO formDataSO;
        try {
            formDataSO = this.formDataFetcher.fetch(this.userId, stepSOExercise.getId());
        } catch (DocumentNotFoundException e) {
            return false;
        }
        return FormDataSOs.isSent(formDataSO);
    }

    private void setAccessibilityStatusForModulesAndSubmodules(ProgramSO programSO) {
        for (ModuleSO moduleSO : programSO.getModules()) {
            for (SubmoduleSO submoduleSO : moduleSO.getSubmodules()) {
                StepSO stepSOFirst = submoduleSO.getSteps().get(0);
                submoduleSO.setAccessible(stepSOFirst.isAccessible());
            }
            SubmoduleSO submoduleSOFirst = moduleSO.getSubmodules().get(0);
            moduleSO.setAccessible(submoduleSOFirst.isAccessible());
        }
    }

}
