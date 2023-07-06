package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public abstract class InitHtmlChunkProducer extends HtmlChunkProducer {

    public InitHtmlChunkProducer(HtmlChunk htmlChunk, HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(htmlChunk, htmlChunkReader, applicationConfigGenericSO);
    }

}
