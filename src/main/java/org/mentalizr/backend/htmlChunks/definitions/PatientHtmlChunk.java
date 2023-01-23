package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.producer.PatientHtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.types.InternalHtmlChunk;

public class PatientHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "PATIENT";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/patient.chunk.html";
    }

    @Override
    public HtmlChunkProducer getProducer() {
        return new PatientHtmlChunkProducer();
    }

}
