package org.mentalizr.backend.security.session.attributes.staging;

import org.mentalizr.backend.security.session.attributes.staging.requirements.Requirement;
import org.mentalizr.backend.security.session.attributes.staging.requirements.Requirements;

public class StagingIntermediate extends StagingAttribute {

    private final Requirements requirements;

    public StagingIntermediate(Requirements requirements) {
        this.requirements = requirements;
    }

    public boolean hasRequirement() {
        return this.requirements.hasRequirements();
    }

    public String getNextRequirementAsString() {
        Requirement requirement = this.requirements.getNextRequirement();
        return requirement.getName();
    }

    public String toString() {
        return "INTERMEDIATE " + this.requirements.toString();
    }

}
