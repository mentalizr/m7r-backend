package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.applicationContext.PolicyCache;
import org.mentalizr.backend.htmlChunks.definitions.hierarchy.ExternalHtmlChunk;

public class PolicyModalHtmlChunk extends ExternalHtmlChunk {

    public static final String NAME = "POLICY_MODAL";

    private final PolicyCache policyCache;

    public PolicyModalHtmlChunk(PolicyCache policyCache) {
        this.policyCache = policyCache;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return this.policyCache.getPolicyFileName();
    }

}
