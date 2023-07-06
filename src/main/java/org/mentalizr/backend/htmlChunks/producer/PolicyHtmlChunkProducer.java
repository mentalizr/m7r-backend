package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.applicationContext.PolicyCache;
import org.mentalizr.backend.htmlChunks.definitions.PolicyHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class PolicyHtmlChunkProducer extends HtmlChunkProducer {

    public PolicyHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO, PolicyCache policyCache) {
        super(new PolicyHtmlChunk(policyCache), htmlChunkReader, applicationConfigGenericSO);
    }

}
