package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.producer.LoginVoucherHtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;

public class LoginVoucherHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "LOGIN_VOUCHER";

    public LoginVoucherHtmlChunk(HtmlChunkReader htmlChunkReader) {
        super(htmlChunkReader);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "WEB-INF/loginVoucher.chunk.html";
    }

}
