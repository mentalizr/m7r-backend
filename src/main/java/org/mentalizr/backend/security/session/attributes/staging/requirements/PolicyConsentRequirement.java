package org.mentalizr.backend.security.session.attributes.staging.requirements;

import org.mentalizr.serviceObjects.SessionStatusSO;

public class PolicyConsentRequirement implements Requirement {

    @Override
    public String getName() {
        return SessionStatusSO.REQUIRE_POLICY_CONSENT;
    }

}
