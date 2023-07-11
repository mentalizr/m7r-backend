package org.mentalizr.backend.htmlChunks.modifier;

import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public interface HtmlChunkModifier {

    String modify(String htmlChunkString, ApplicationConfigGenericSO applicationConfigGenericSO);

}
