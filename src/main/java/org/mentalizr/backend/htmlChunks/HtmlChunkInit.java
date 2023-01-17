package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;

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

        InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
        String titel = instanceConfiguration.getApplicationConfigGenericSO().getTitle();
        htmlChunkModifierInit.addTitle(titel);

        return htmlChunkModifierInit.getModifiedChunk();
    }

}
