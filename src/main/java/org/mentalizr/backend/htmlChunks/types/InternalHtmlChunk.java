package org.mentalizr.backend.htmlChunks.types;

import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.backend.htmlChunks.reader.InternalHtmlChunkReader;

import javax.servlet.ServletContext;

public abstract class InternalHtmlChunk extends HtmlChunk {

    @Override
    public HtmlChunkReader getReader(ServletContext servletContext) {
        return new InternalHtmlChunkReader(this, servletContext);
    }

}
