package org.mentalizr.backend.adapter;

import org.mentalizr.contentManager.programStructure.*;
import org.mentalizr.contentManager.programStructure.Module;
import org.mentalizr.serviceObjects.frontend.program.*;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdapter {

    public static ProgramSO getProgramSO(ProgramStructure programStructure) {

        ProgramSO programSO = new ProgramSO();
        programSO.setId(programStructure.getId());
        programSO.setName(programStructure.getName());

        List<InfotextSO> infotextSOList = getInfotextSOList(programStructure.getInfotexts());
        programSO.setInfotexts(infotextSOList);

        List<ModuleSO> moduleSOList = getModuleSOList(programStructure.getModules());
        programSO.setModules(moduleSOList);

        return programSO;
    }

    private static List<InfotextSO> getInfotextSOList(List<Infotext> infotextList) {
        List<InfotextSO> infotextSOList = new ArrayList<>();
        for (Infotext infotext : infotextList) {
            InfotextSO infotextSO = new InfotextSO();
            infotextSO.setId(infotext.getId());
            infotextSO.setName(infotext.getName());
            infotextSOList.add(infotextSO);
        }
        return infotextSOList;
    }

    private static List<ModuleSO> getModuleSOList(List<Module> moduleList) {
        List<ModuleSO> moduleSOList = new ArrayList<>();
        for (Module module : moduleList) {
            ModuleSO moduleSO = new ModuleSO();
            moduleSO.setId(module.getId());
            moduleSO.setName(moduleSO.getName());
            moduleSO.setSubmodules(getSubmoduleSOList(module.getSubmodules()));
            moduleSOList.add(moduleSO);
        }
        return moduleSOList;
    }

    private static List<SubmoduleSO> getSubmoduleSOList(List<Submodule> submoduleList) {
        List<SubmoduleSO> submoduleSOList = new ArrayList<>();
        for (Submodule submodule : submoduleList) {
            SubmoduleSO submoduleSO = new SubmoduleSO();
            submoduleSO.setId(submodule.getId());
            submoduleSO.setName(submodule.getName());
            submoduleSO.setSteps(getStepSOList(submodule.getSteps()));
            submoduleSOList.add(submoduleSO);
        }
        return submoduleSOList;
    }

    private static List<StepSO> getStepSOList(List<Step> stepList) {
        List<StepSO> stepSOList = new ArrayList<>();
        for (Step step : stepList) {
            StepSO stepSO = new StepSO();
            stepSO.setId(step.getId());
            stepSO.setName(step.getName());
            stepSOList.add(stepSO);
        }
        return stepSOList;
    }

}
