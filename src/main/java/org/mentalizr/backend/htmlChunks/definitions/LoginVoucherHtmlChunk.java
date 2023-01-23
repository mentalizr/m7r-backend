package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.producer.LoginVoucherHtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.types.InternalHtmlChunk;

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
    public HtmlChunkProducer getProducer() {
        return new LoginVoucherHtmlChunkProducer();
    }

}
