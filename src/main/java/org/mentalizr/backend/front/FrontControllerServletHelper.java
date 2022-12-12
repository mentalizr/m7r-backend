package org.mentalizr.backend.front;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.htmlChunks.HtmlChunkModifierInit;

public class FrontControllerServletHelper {

    public static void addTitle(HtmlChunkModifierInit htmlChunkModifierInit) {
        InstanceConfiguration instanceConfiguration = ApplicationContext.getBrandingConfiguration();
        String titel = instanceConfiguration.getApplicationConfigGenericSO().getTitle();
        htmlChunkModifierInit.addTitle(titel);
    }

}
