package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.htmlChunks.definitions.LoginHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.LoginHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class LoginHtmlChunkProducer extends HtmlChunkProducer {

    public LoginHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(new LoginHtmlChunk(htmlChunkReader), applicationConfigGenericSO);
    }

//    @Override
//    public void modify() {
//        String logo = this.applicationConfigGenericSO.getLogo();
//        LoginHtmlChunkModifier loginHtmlChunkModifier = (LoginHtmlChunkModifier) this.htmlChunkModifier;
//        loginHtmlChunkModifier.addLogo(logo);
//    }

}
