package org.mentalizr.backend.htmlChunks.reader;

import org.mentalizr.backend.htmlChunks.types.HtmlChunk;

import javax.servlet.ServletContext;
import java.io.InputStream;

public abstract class HtmlChunkReader {

    protected final HtmlChunk htmlChunk;
    protected final ServletContext servletContext;

    public HtmlChunkReader(HtmlChunk htmlChunk, ServletContext servletContext) {
        this.htmlChunk = htmlChunk;
        this.servletContext = servletContext;
    }

    public abstract String asString();

}
