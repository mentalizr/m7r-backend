package org.mentalizr.backend.htmlChunks.reader;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;

public interface HtmlChunkReader {

    String fromWebAppResource(HtmlChunk htmlChunk);

    String fromPolicyConfiguration();

}
