package org.mentalizr.backend.htmlChunks.modifier;

import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class NoopModifier implements HtmlChunkModifier {

    @Override
    public String modify(String htmlChunkString, ApplicationConfigGenericSO applicationConfigGenericSO) {
        return htmlChunkString;
    }
}
