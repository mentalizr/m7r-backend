package org.mentalizr.backend.htmlChunks.modifier;

public abstract class HtmlChunkModifier {

    protected String chunk;

    public void setRawChunk(String chunk) {
        this.chunk = chunk;
    }

    public String getModifiedChunk() {
        return this.chunk;
    }

}
