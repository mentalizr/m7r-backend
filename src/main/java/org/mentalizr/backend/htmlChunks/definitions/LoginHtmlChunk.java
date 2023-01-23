package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.modifier.LoginHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.producer.LoginHtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.types.InternalHtmlChunk;

public class LoginHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "LOGIN";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/login.chunk.html";
    }

    @Override
    public LoginHtmlChunkModifier getModifier() {
        return new LoginHtmlChunkModifier();
    }

    @Override
    public HtmlChunkProducer getProducer() {
        return new LoginHtmlChunkProducer();
    }

//    @Override
//    public String asString(ServletContext servletContext) {
//        HtmlChunkReader htmlChunkReader = this.getReader(servletContext);
//        String chunk = htmlChunkReader.asString();
//
//        HtmlChunkModifierLogin htmlChunkModifierLogin = this.getModifier();
//        htmlChunkModifierLogin.setRawChunk(chunk);
//        InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
//        String logo = instanceConfiguration.getApplicationConfigGenericSO().getLogo();
//        htmlChunkModifierLogin.addLogo(logo);
//
//        return htmlChunkModifierLogin.getModifiedChunk();
//    }

}
