package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.htmlChunks.definitions.LoginHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.LoginVoucherHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.LoginHtmlChunkModifier;

public class LoginVoucherHtmlChunkProducer extends HtmlChunkProducer {

    public LoginVoucherHtmlChunkProducer() {
        super(LoginVoucherHtmlChunk.NAME);
    }

    @Override
    public void modify() {
        InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
        String logo = instanceConfiguration.getApplicationConfigGenericSO().getLogo();
        LoginHtmlChunkModifier loginHtmlChunkModifier = (LoginHtmlChunkModifier) this.htmlChunkModifier;
        loginHtmlChunkModifier.addLogo(logo);
    }

}
