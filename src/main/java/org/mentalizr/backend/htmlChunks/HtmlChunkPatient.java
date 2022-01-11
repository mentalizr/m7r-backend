package org.mentalizr.backend.htmlChunks;

import javax.servlet.ServletContext;
import java.io.IOException;

public class HtmlChunkPatient extends HtmlChunk {

    public static final String NAME = "PATIENT";

    public HtmlChunkPatient(ServletContext servletContext) throws IOException {
        super(servletContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/patient.chunk.html";
    }

    @Override
    public String modifyChunk(String chunk) {
        return chunk;
    }

}
