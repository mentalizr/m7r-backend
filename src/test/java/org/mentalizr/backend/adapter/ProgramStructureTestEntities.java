package org.mentalizr.backend.adapter;

import org.mentalizr.contentManager.programStructure.Module;
import org.mentalizr.contentManager.programStructure.ProgramStructure;
import org.mentalizr.contentManager.programStructure.Step;
import org.mentalizr.contentManager.programStructure.Submodule;

import java.util.ArrayList;
import java.util.List;

public class ProgramStructureTestEntities {

    public static ProgramStructure withFeedback() {

        List<Step> stepListSM1 = new ArrayList<>();
        Step step = new Step("m1_sm1_s1", "step 1", false, false);
        stepListSM1.add(step);
        Step step2 = new Step("m1_sm1_s2", "step 2", true, false);
        stepListSM1.add(step2);
        Step step3 = new Step("m1_sm1_s3", "step 3", false, true);
        stepListSM1.add(step3);
        Submodule submodule1 = new Submodule("m1_sm1", "submodule 1", stepListSM1);

        List<Step> stepListSM2 = new ArrayList<>();
        Step step4 = new Step("m1_sm2_s1", "step 4", false, false);
        stepListSM2.add(step4);
        Step step5 = new Step("m1_sm2_s2", "step 5", true, false);
        stepListSM2.add(step5);
        Step step6 = new Step("m1_sm2_s3", "step 6", false, true);
        stepListSM2.add(step6);
        Step step7 = new Step("m1_sm2_s4", "step 7", false, false);
        stepListSM2.add(step7);

        Submodule submodule2 = new Submodule("m1_sm2", "submodule 2", stepListSM2);

        List<Submodule> submoduleList = new ArrayList<>();
        submoduleList.add(submodule1);
        submoduleList.add(submodule2);

        Module module = new Module("m1", "module 1", submoduleList);

        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);

        return new ProgramStructure("p1", "test program", moduleList);
    }

}
