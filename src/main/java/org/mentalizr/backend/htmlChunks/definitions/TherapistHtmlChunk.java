package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;

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

}
