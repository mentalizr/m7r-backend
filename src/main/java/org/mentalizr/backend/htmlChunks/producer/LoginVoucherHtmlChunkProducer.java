package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.LoginVoucherHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class LoginVoucherHtmlChunkProducer extends HtmlChunkProducer {

    public LoginVoucherHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(new LoginVoucherHtmlChunk(), htmlChunkReader, applicationConfigGenericSO);
    }

}
