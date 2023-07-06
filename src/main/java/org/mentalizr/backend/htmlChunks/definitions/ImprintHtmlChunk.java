package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.ExternalHtmlChunk;

public class ImprintHtmlChunk extends ExternalHtmlChunk {

    public static final String NAME = "IMPRINT";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "imprint.html";
    }

}
