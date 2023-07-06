package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;

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

}
