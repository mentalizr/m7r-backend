package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.BrandingConfiguration;
import org.mentalizr.backend.config.Configuration;

import javax.servlet.ServletContext;
import java.io.IOException;

public class HtmlChunkInit extends HtmlChunk {

    public static final String NAME = "INIT";

    public HtmlChunkInit(ServletContext servletContext) throws IOException {
        super(servletContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/init.html";
    }

    @Override
    public String modifyChunk(String chunk) {
        HtmlChunkModifierInit htmlChunkModifierInit = new HtmlChunkModifierInit(chunk);

        BrandingConfiguration brandingConfiguration = ApplicationContext.getBrandingConfiguration();
        String titel = brandingConfiguration.getApplicationConfigGenericSO().getTitle();
        htmlChunkModifierInit.addTitle(titel);

        return htmlChunkModifierInit.getModifiedChunk();
    }

}
