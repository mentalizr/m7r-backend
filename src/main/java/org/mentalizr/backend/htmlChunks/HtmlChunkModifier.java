package org.mentalizr.backend.htmlChunks;

public abstract class HtmlChunkModifier {

    protected String chunk;

    public HtmlChunkModifier(String chunk) {
        this.chunk = chunk;
    }

    public String getModifiedChunk() {
        return this.chunk;
    }

}
