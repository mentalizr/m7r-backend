package org.mentalizr.backend.adapter;

import org.mentalizr.serviceObjects.frontend.program.StepSO;

import java.util.List;

public class ProgramSOListTestHelper {

    public static boolean isAccessibleUntil(List<StepSO> stepSOList, String id) {

        boolean found = false;
        for (StepSO stepSO : stepSOList) {
            if (!found && !stepSO.isAccessible()) return false;
            if (found && stepSO.isAccessible()) return false;
            if (stepSO.getId().equals(id)) found = true;
        }

        if (!found) throw new RuntimeException("Id [" + id + "] not found in specified StepSOList.");

        return true;
    }


}
