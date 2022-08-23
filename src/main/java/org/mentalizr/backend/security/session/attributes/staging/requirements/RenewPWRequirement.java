package org.mentalizr.backend.security.session.attributes.staging.requirements;

import org.mentalizr.serviceObjects.SessionStatusSO;

public class RenewPWRequirement implements Requirement {

    @Override
    public String getName() {
        return SessionStatusSO.REQUIRE_RENEW_PASSWORD;
    }
}
