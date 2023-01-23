package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.producer.InitLoginHtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.types.InternalHtmlChunk;

public class InitLoginHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "INIT";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/init.html";
    }

    @Override
    public InitHtmlChunkModifier getModifier() {
        return new InitHtmlChunkModifier();
    }

    @Override
    public HtmlChunkProducer getProducer() {
        return new InitLoginHtmlChunkProducer();
    }

}
