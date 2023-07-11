package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.applicationContext.PolicyCache;
import org.mentalizr.backend.htmlChunks.definitions.PolicyConsentHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.PolicyModalHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class PolicyModalHtmlChunkProducer extends HtmlChunkProducer {

    public PolicyModalHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO, PolicyCache policyCache) {
        super(new PolicyModalHtmlChunk(policyCache), htmlChunkReader, applicationConfigGenericSO);
    }

}
