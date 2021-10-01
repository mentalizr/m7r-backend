package org.mentalizr.backend.adapter;

import org.mentalizr.serviceObjects.frontend.program.ModuleSO;
import org.mentalizr.serviceObjects.frontend.program.ProgramSO;
import org.mentalizr.serviceObjects.frontend.program.StepSO;
import org.mentalizr.serviceObjects.frontend.program.SubmoduleSO;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdapterUtils {

    public static List<StepSO> buildStepSOList(ProgramSO programSO) {
        List<StepSO> stepSOList = new ArrayList<>();
        for (ModuleSO moduleSO : programSO.getModules()) {
            for (SubmoduleSO submoduleSO : moduleSO.getSubmodules()) {
                stepSOList.addAll(submoduleSO.getSteps());
            }
        }
        return stepSOList;
    }





}
