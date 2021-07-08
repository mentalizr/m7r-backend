package org.mentalizr.backend.htmlChunks;

public class InitChunkModifier {

    private final String initChunk;

    public InitChunkModifier(String initChunk) {
        this.initChunk = initChunk;
    }

    public String withEntry(HtmlChunk htmlChunk) {
        return this.initChunk.replace("<meta name=\"entry\" content=\"\">", "<meta name=\"entry\" content=\"" + htmlChunk.name() + "\">");
    }
}
