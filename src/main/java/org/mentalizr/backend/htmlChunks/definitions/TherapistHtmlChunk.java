package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

public class TherapistHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "THERAPIST";

    public TherapistHtmlChunk(HtmlChunkReader htmlChunkReader) {
        super(htmlChunkReader);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/therapist.chunk.html";
    }

}
