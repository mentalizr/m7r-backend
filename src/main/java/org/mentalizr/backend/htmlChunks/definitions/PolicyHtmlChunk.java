package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

public class PolicyHtmlChunk extends HtmlChunk {

    public static final String NAME = "POLICY";

    public PolicyHtmlChunk(HtmlChunkReader htmlChunkReader) {
        super(htmlChunkReader);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String asString() {
        return this.htmlChunkReader.fromPolicyConfiguration();
    }

}
