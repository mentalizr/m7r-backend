package org.mentalizr.backend.security.session.attributes.staging.requirements;

import org.mentalizr.serviceObjects.SessionStatusSO;

public class EmailConfirmationRequirement implements Requirement {

    private final String token;
    private final String code;

    public EmailConfirmationRequirement(String token, String code) {
        this.token = token;
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return SessionStatusSO.REQUIRE_EMAIL_CONFIRMATION;
    }
}
