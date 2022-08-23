package org.mentalizr.backend.security.session.attributes.staging.requirements;


import java.util.LinkedHashSet;
import java.util.Optional;

public class Requirements {

    private final LinkedHashSet<Requirement> requirementsSet;

    public Requirements() {
        this.requirementsSet = new LinkedHashSet<>();
    }

    public void add2FARequirement() {
        this.requirementsSet.add(new SecondFARequirement());
    }

    public void addPolicyConsentRequirement() {
        this.requirementsSet.add(new PolicyConsentRequirement());
    }

    public void addEmailConfirmationRequirement(String token, String code) {
        this.requirementsSet.add(new EmailConfirmationRequirement(token, code));
    }

    public void addRenewPasswordRequirement() {
        this.requirementsSet.add(new RenewPWRequirement());
    }

    public boolean hasRequirements() {
        return !this.requirementsSet.isEmpty();
    }

    public Requirement getNextRequirement() {
        Optional<Requirement> requirementOptional = this.requirementsSet.stream().findFirst();
        if (requirementOptional.isEmpty()) throw new IllegalStateException("No next requirement.");
        return requirementOptional.get();
    }

    public void markNextRequirementAsFulfilled() {
        Requirement requirement = getNextRequirement();
        this.requirementsSet.remove(requirement);
    }

}
