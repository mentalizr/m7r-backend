package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.applicationContext.PolicyCache;
import org.mentalizr.backend.htmlChunks.producer.*;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.backend.utils.StringUtils;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HtmlChunkCache {

    private final HtmlChunkMap chunkCacheMap;

    public HtmlChunkCache(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO, PolicyCache policyCache) {
        this.chunkCacheMap = new HtmlChunkMap();

        putHtmlChunk(new InitLoginHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO));
        putHtmlChunk(new InitLoginVoucherHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO));
        putHtmlChunk(new LoginHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO));
        putHtmlChunk(new LoginVoucherHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO));
        putHtmlChunk(new PolicyConsentHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO, policyCache));
        putHtmlChunk(new PolicyModalHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO, policyCache));
        putHtmlChunk(new ImprintHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO));
        putHtmlChunk(new PatientHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO));
        putHtmlChunk(new TherapistHtmlChunkProducer(htmlChunkReader, applicationConfigGenericSO));
    }

    public String getChunkAsString(String name) {
        if (this.chunkCacheMap.contains(name))
            return this.chunkCacheMap.getChunkAsString(name);

        throw new RuntimeException("Unknown HtmlChunk: [" + name + "].");
    }

    private void putHtmlChunk(HtmlChunkProducer htmlChunkProducer) {
        String chunkName = htmlChunkProducer.getChunkName();
        String chunkString = htmlChunkProducer.getHtml();
        this.chunkCacheMap.put(chunkName, chunkString);
    }

}
