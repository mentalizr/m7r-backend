package org.mentalizr.backend.front;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.BrandingConfiguration;
import org.mentalizr.backend.htmlChunks.HtmlChunkModifierInit;

public class FrontControllerServletHelper {

    public static void addTitle(HtmlChunkModifierInit htmlChunkModifierInit) {
        BrandingConfiguration brandingConfiguration = ApplicationContext.getBrandingConfiguration();
        String titel = brandingConfiguration.getApplicationConfigGenericSO().getTitle();
        htmlChunkModifierInit.addTitle(titel);
    }

}
