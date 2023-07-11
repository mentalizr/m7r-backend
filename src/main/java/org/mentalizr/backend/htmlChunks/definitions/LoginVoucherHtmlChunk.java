package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.LoginHtmlChunkModifier;

public class LoginVoucherHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "LOGIN_VOUCHER";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "WEB-INF/loginVoucher.chunk.html";
    }

    @Override
    public LoginHtmlChunkModifier getModifier() {
        return new LoginHtmlChunkModifier();
    }

}
