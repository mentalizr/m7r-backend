package org.mentalizr.backend.security.session.attributes.staging;

public class StagingIntermediate extends StagingAttribute {

    private final boolean secondFactorRequired;
    private final boolean passwordChangeRequired;
    private final boolean consentPoliciesRequired;
    private final boolean emailConfirmationRequired;

    public StagingIntermediate(boolean secondFactorRequired, boolean passwordChangeRequired, boolean consentPoliciesRequired, boolean emailConfirmationRequired) {
        this.secondFactorRequired = secondFactorRequired;
        this.passwordChangeRequired = passwordChangeRequired;
        this.consentPoliciesRequired = consentPoliciesRequired;
        this.emailConfirmationRequired = emailConfirmationRequired;
    }

    public boolean hasRequirement() {
        return this.secondFactorRequired || this.passwordChangeRequired || this.consentPoliciesRequired || this.emailConfirmationRequired;
    }

    public boolean isSecondFactorRequired() {
        return this.secondFactorRequired;
    }

    public boolean isPasswordChangeRequired() {
        return this.passwordChangeRequired;
    }

    public boolean isConsentPoliciesRequired() {
        return this.consentPoliciesRequired;
    }

    public boolean isEmailConfirmationRequired() {
        return this.emailConfirmationRequired;
    }

}
