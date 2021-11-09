package org.mentalizr.backend.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class MentalizrRestApplication extends ResourceConfig {
    public MentalizrRestApplication() {
        packages(
                "org.mentalizr.backend.rest.endpoints",
                "org.mentalizr.backend.rest.endpoints.patient",
                "org.mentalizr.backend.rest.endpoints.patient.formData"
        );
    }
}