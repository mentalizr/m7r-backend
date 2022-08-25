package org.mentalizr.backend.security.session.attributes.staging.requirements;


import de.arthurpicht.utils.core.strings.Strings;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        List<String> names = this.requirementsSet.stream().map(Requirement::getName).collect(Collectors.toList());
        return "Requirements: " + Strings.listing(names, ", ") + ".";
    }

}
