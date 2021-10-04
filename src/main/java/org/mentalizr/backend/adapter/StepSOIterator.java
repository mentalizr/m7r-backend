package org.mentalizr.backend.adapter;

import org.mentalizr.serviceObjects.frontend.program.StepSO;

import java.util.List;

public class StepSOIterator {

    private final List<StepSO> stepSOList;
    private int index = -1;

    public StepSOIterator(List<StepSO> stepSOList) {
        this.stepSOList = stepSOList;
    }

    public boolean hasNext() {
        return this.stepSOList.size() > this.index + 1;
    }

    public StepSO getNext() {
        this.index++;
        return this.stepSOList.get(this.index);
    }

    public StepSO getCurrent() {
        if (this.index < 0) throw new IllegalStateException("First element of StepSOIterator not obtained.");
        return this.stepSOList.get(this.index);
    }

    public boolean hasPrevious() {
        return this.index > 0;
    }

    public StepSO getPrevious() {
        this.index--;
        return this.stepSOList.get(this.index);
    }

    public int getIndex() {
        return this.index;
    }

    public List<StepSO> getStepSOList() {
        return this.stepSOList;
    }

    public boolean hasSubsequentFeedbackStep() {
        if (!hasNext()) return false;
        StepSO nextStepSO = this.stepSOList.get(index + 1);
        return nextStepSO.isFeedback();
    }

    public boolean hasPrecedingExerciseStep() {
        if (!hasPrevious()) return false;
        StepSO previousStepSO = this.stepSOList.get(index - 1);
        return previousStepSO.isExercise();
    }

}
