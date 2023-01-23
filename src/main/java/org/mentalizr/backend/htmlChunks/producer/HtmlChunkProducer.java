package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.htmlChunks.HtmlChunkCache;
import org.mentalizr.backend.htmlChunks.modifier.HtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.types.HtmlChunk;

public abstract class HtmlChunkProducer {

    protected final String chunkName;
    protected final HtmlChunkModifier htmlChunkModifier;
    protected final String templateHtml;
    protected final String productHtml;

    public HtmlChunkProducer(String chunkName) {
        this.chunkName = chunkName;

        HtmlChunkCache htmlChunkCache = ApplicationContext.getHtmlChunkCache();
        HtmlChunk htmlChunk = htmlChunkCache.getHtmlChunk(chunkName);

        this.templateHtml = htmlChunkCache.getHtmlChunkAsString(chunkName);
        this.htmlChunkModifier = htmlChunk.getModifier();
        this.htmlChunkModifier.setRawChunk(templateHtml);

        modify();

        this.productHtml = this.htmlChunkModifier.getModifiedChunk();
    }

    public String getHtml() {
        return this.productHtml;
    }

    public abstract void modify();

}
