package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.HtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public abstract class HtmlChunkProducer {

    protected final String chunkName;
    protected final HtmlChunkModifier htmlChunkModifier;
    protected final String templateHtml;
    protected final String productHtml;

    public HtmlChunkProducer(HtmlChunk htmlChunk, HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        this.chunkName = htmlChunk.getName();
        this.templateHtml = htmlChunkReader.asString(htmlChunk);
        this.htmlChunkModifier = htmlChunk.getModifier();
        this.productHtml = this.htmlChunkModifier.modify(this.templateHtml, applicationConfigGenericSO);
    }

    public String getChunkName() {
        return this.chunkName;
    }

    public String getHtml() {
        return this.productHtml;
    }

}
