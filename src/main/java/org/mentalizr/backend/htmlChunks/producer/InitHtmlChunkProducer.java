package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public abstract class InitHtmlChunkProducer extends HtmlChunkProducer {

    public InitHtmlChunkProducer(HtmlChunk htmlChunk, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(htmlChunk, applicationConfigGenericSO);
    }

}
