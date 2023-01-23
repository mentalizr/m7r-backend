package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.LoginHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;

public class InitLoginHtmlChunkProducer extends InitHtmlChunkProducer {

    @Override
    public void addEntry(InitHtmlChunkModifier initHtmlChunkModifier) {
        initHtmlChunkModifier.addEntry(LoginHtmlChunk.NAME);
    }

}
