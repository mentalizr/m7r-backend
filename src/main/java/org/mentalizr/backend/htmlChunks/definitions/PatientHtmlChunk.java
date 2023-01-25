package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

public class PatientHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "PATIENT";

    public PatientHtmlChunk(HtmlChunkReader htmlChunkReader) {
        super(htmlChunkReader);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/patient.chunk.html";
    }

}
