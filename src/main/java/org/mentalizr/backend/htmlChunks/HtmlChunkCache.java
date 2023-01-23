package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.htmlChunks.definitions.*;
import org.mentalizr.backend.htmlChunks.types.HtmlChunk;

import javax.servlet.ServletContext;

public class HtmlChunkCache {

    private final ServletContext servletContext;
    private final HtmlChunkMap chunkCacheMap;

    public HtmlChunkCache(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.chunkCacheMap = new HtmlChunkMap();

        putHtmlChunk(new InitLoginHtmlChunk());
        putHtmlChunk(new InitVoucherHtmlChunk());
        putHtmlChunk(new LoginHtmlChunk());
        putHtmlChunk(new LoginVoucherHtmlChunk());
        putHtmlChunk(new PolicyHtmlChunk());
        putHtmlChunk(new PatientHtmlChunk());
        putHtmlChunk(new TherapistHtmlChunk());
    }

    private void putHtmlChunk(HtmlChunk htmlChunk) {
        this.chunkCacheMap.put(htmlChunk, this.servletContext);
    }

    public HtmlChunk getHtmlChunk(String name) {
        if (this.chunkCacheMap.contains(name))
            return this.chunkCacheMap.getHtmlChunk(name);

        throw new RuntimeException("Unknown HtmlChunk: [" + name + "].");
    }

    public String getHtmlChunkAsString(String name) {
        if (this.chunkCacheMap.contains(name))
            return this.chunkCacheMap.getChunkAsString(name);

        throw new RuntimeException("Unknown HtmlChunk: [" + name + "].");
    }

}
