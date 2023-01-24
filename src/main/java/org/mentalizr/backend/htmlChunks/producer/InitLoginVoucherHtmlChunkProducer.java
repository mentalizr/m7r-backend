package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.InitVoucherHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.LoginVoucherHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class InitLoginVoucherHtmlChunkProducer extends InitHtmlChunkProducer {

    public InitLoginVoucherHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(new InitVoucherHtmlChunk(htmlChunkReader), applicationConfigGenericSO);
    }

//    @Override
//    public void addEntry(InitHtmlChunkModifier initHtmlChunkModifier) {
//        initHtmlChunkModifier.addEntry(LoginVoucherHtmlChunk.NAME);
//    }

}
