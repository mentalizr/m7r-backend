package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;

import javax.servlet.ServletContext;
import java.io.IOException;

public class HtmlChunkLogin extends HtmlChunk {

    public static final String NAME = "LOGIN";

    public HtmlChunkLogin(ServletContext servletContext) throws IOException {
        super(servletContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/login.chunk.html";
    }

    @Override
    public String modifyChunk(String chunk) {
        InstanceConfiguration instanceConfiguration = ApplicationContext.getBrandingConfiguration();
        String logo = instanceConfiguration.getApplicationConfigGenericSO().getLogo();
        HtmlChunkModifierLogin htmlChunkModifierLogin = new HtmlChunkModifierLogin(chunk);
        htmlChunkModifierLogin.addLogo(logo);
        return htmlChunkModifierLogin.getModifiedChunk();
    }

}
