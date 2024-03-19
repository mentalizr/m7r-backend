package org.mentalizr.backend.programSOCreator;

import org.mentalizr.contentManager.programStructure.*;
import org.mentalizr.contentManager.programStructure.Module;
import org.mentalizr.serviceObjects.frontend.program.*;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdapter {

    public static ProgramSO getProgramSO(ProgramStructure programStructure) {

        ProgramSO programSO = new ProgramSO();
        programSO.setId(programStructure.id());
        programSO.setName(programStructure.name());

        List<InfotextSO> infotextSOList = getInfotextSOList(programStructure.infotexts());
        programSO.setInfotexts(infotextSOList);

        List<ModuleSO> moduleSOList = getModuleSOList(programStructure.modules());
        programSO.setModules(moduleSOList);

        return programSO;
    }

    private static List<InfotextSO> getInfotextSOList(List<Infotext> infotextList) {
        List<InfotextSO> infotextSOList = new ArrayList<>();
        for (Infotext infotext : infotextList) {
            InfotextSO infotextSO = new InfotextSO();
            infotextSO.setId(infotext.id());
            infotextSO.setName(infotext.name());
            infotextSOList.add(infotextSO);
        }
        return infotextSOList;
    }

    private static List<ModuleSO> getModuleSOList(List<Module> moduleList) {
        List<ModuleSO> moduleSOList = new ArrayList<>();
        for (Module module : moduleList) {
            ModuleSO moduleSO = new ModuleSO();
            moduleSO.setId(module.id());
            moduleSO.setName(module.name());
            moduleSO.setSubmodules(getSubmoduleSOList(module.submodules()));
            moduleSOList.add(moduleSO);
        }
        return moduleSOList;
    }

    private static List<SubmoduleSO> getSubmoduleSOList(List<Submodule> submoduleList) {
        List<SubmoduleSO> submoduleSOList = new ArrayList<>();
        for (Submodule submodule : submoduleList) {
            SubmoduleSO submoduleSO = new SubmoduleSO();
            submoduleSO.setId(submodule.id());
            submoduleSO.setName(submodule.name());
            submoduleSO.setSteps(getStepSOList(submodule.steps()));
            submoduleSOList.add(submoduleSO);
        }
        return submoduleSOList;
    }

    private static List<StepSO> getStepSOList(List<Step> stepList) {
        List<StepSO> stepSOList = new ArrayList<>();
        for (Step step : stepList) {
            StepSO stepSO = new StepSO();
            stepSO.setId(step.id());
            stepSO.setName(step.name());
            stepSO.setExercise(step.exercise());
            stepSO.setFeedback(step.feedback());
            stepSOList.add(stepSO);
        }
        return stepSOList;
    }

}
