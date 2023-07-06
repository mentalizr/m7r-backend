package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.applicationContext.PolicyCache;
import org.mentalizr.backend.htmlChunks.definitions.PolicyConsentHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class PolicyConsentHtmlChunkProducer extends HtmlChunkProducer {

    public PolicyConsentHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO, PolicyCache policyCache) {
        super(new PolicyConsentHtmlChunk(policyCache), htmlChunkReader, applicationConfigGenericSO);
    }

}
