package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.producer.PolicyHtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.types.InternalHtmlChunk;

public class PolicyHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "POLICY";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/policy.chunk.html";
    }

    @Override
    public HtmlChunkProducer getProducer() {
        return new PolicyHtmlChunkProducer();
    }

}
