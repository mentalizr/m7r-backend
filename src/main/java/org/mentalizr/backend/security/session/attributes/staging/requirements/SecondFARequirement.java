package org.mentalizr.backend.security.session.attributes.staging.requirements;

import org.mentalizr.serviceObjects.SessionStatusSO;

public class SecondFARequirement implements Requirement {

    @Override
    public String getName() {
        return SessionStatusSO.REQUIRE_2FA;
    }

}
