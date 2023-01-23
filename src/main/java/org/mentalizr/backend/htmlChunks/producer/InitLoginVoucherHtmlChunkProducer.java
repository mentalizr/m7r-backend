package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.LoginVoucherHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;

public class InitLoginVoucherHtmlChunkProducer extends InitHtmlChunkProducer {

    @Override
    public void addEntry(InitHtmlChunkModifier initHtmlChunkModifier) {
        initHtmlChunkModifier.addEntry(LoginVoucherHtmlChunk.NAME);
    }

}
