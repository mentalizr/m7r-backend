package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;

import javax.servlet.ServletContext;
import java.io.IOException;

public class HtmlChunkLoginVoucher extends HtmlChunk {

    public static final String NAME = "LOGIN_VOUCHER";

    public HtmlChunkLoginVoucher(ServletContext servletContext) throws IOException {
        super(servletContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "WEB-INF/loginVoucher.chunk.html";
    }

    @Override
    public String modifyChunk(String chunk) {
        InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
        String logo = instanceConfiguration.getApplicationConfigGenericSO().getLogo();
        HtmlChunkModifierLogin htmlChunkModifierLogin = new HtmlChunkModifierLogin(chunk);
        htmlChunkModifierLogin.addLogo(logo);
        return htmlChunkModifierLogin.getModifiedChunk();
    }

}
