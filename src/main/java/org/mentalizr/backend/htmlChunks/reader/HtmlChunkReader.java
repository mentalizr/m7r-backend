package org.mentalizr.backend.htmlChunks.reader;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;

public interface HtmlChunkReader {

    public abstract String fromWebAppResource(HtmlChunk htmlChunk);

    public abstract String fromPolicyConfiguration();

}
