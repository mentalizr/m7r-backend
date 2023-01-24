package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.InitLoginHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.LoginHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class InitLoginHtmlChunkProducer extends InitHtmlChunkProducer {

    public InitLoginHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(new InitLoginHtmlChunk(htmlChunkReader), applicationConfigGenericSO);
    }

//    @Override
//    public void addEntry(InitHtmlChunkModifier initHtmlChunkModifier) {
//        initHtmlChunkModifier.addEntry(LoginHtmlChunk.NAME);
//    }

}
