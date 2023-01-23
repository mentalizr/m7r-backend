package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.producer.TherapistHtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.types.InternalHtmlChunk;

public class TherapistHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "THERAPIST";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/therapist.chunk.html";
    }

    @Override
    public HtmlChunkProducer getProducer() {
        return new TherapistHtmlChunkProducer();
    }

}
