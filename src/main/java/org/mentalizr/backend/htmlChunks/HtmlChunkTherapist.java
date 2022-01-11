package org.mentalizr.backend.htmlChunks;

import javax.servlet.ServletContext;
import java.io.IOException;

public class HtmlChunkTherapist extends HtmlChunk {

    public static final String NAME = "THERAPIST";

    public HtmlChunkTherapist(ServletContext servletContext) throws IOException {
        super(servletContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/therapist.chunk.html";
    }

    @Override
    public String modifyChunk(String chunk) {
        return chunk;
    }

}
