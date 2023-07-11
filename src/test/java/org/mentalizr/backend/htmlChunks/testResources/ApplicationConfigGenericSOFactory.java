package org.mentalizr.backend.htmlChunks.testResources;

import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class ApplicationConfigGenericSOFactory {

    public static ApplicationConfigGenericSO create() {
        ApplicationConfigGenericSO applicationConfigGenericSO = new ApplicationConfigGenericSO();
        applicationConfigGenericSO.setLogo("test-logo.png");
        applicationConfigGenericSO.setTitle("test title");
        return applicationConfigGenericSO;
    }

}
