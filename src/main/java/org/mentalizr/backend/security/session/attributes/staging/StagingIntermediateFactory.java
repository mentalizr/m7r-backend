package org.mentalizr.backend.security.session.attributes.staging;

public class StagingIntermediateFactory {

    public static StagingIntermediate withSecondFactorOK(StagingIntermediate stagingIntermediate) {
        return new StagingIntermediate(
                false,
                stagingIntermediate.isPasswordChangeRequired(),
                stagingIntermediate.isConsentPoliciesRequired(),
                stagingIntermediate.isEmailConfirmationRequired()
        );
    }

    public static StagingIntermediate withPasswordChangeOK(StagingIntermediate stagingIntermediate) {
        return new StagingIntermediate(
                stagingIntermediate.isSecondFactorRequired(),
                false,
                stagingIntermediate.isConsentPoliciesRequired(),
                stagingIntermediate.isEmailConfirmationRequired()
        );
    }

    public static StagingIntermediate withConsentPoliciesOK(StagingIntermediate stagingIntermediate) {
        return new StagingIntermediate(
                stagingIntermediate.isSecondFactorRequired(),
                stagingIntermediate.isPasswordChangeRequired(),
                false,
                stagingIntermediate.isEmailConfirmationRequired()
        );
    }

    public static StagingIntermediate withEmailConfirmationOK(StagingIntermediate stagingIntermediate) {
        return new StagingIntermediate(
                stagingIntermediate.isSecondFactorRequired(),
                stagingIntermediate.isPasswordChangeRequired(),
                stagingIntermediate.isConsentPoliciesRequired(),
                false
        );
    }

}
