package org.mentalizr.backend.htmlChunks.definitions.hierarchy;

import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

public abstract class InternalHtmlChunk extends HtmlChunk {

    public InternalHtmlChunk(HtmlChunkReader htmlChunkReader) {
        super(htmlChunkReader);
    }

    public abstract String getFileName();

    @Override
    public String asString() {
        return this.htmlChunkReader.fromWebAppResource(this);
    }

}
