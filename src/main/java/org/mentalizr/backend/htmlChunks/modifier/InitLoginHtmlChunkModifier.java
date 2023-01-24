package org.mentalizr.backend.htmlChunks.modifier;

import org.mentalizr.backend.htmlChunks.definitions.LoginHtmlChunk;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class InitLoginHtmlChunkModifier extends InitHtmlChunkModifier implements HtmlChunkModifier {

    @Override
    public String modify(String htmlChunkString, ApplicationConfigGenericSO applicationConfigGenericSO) {
        htmlChunkString = super.modify(htmlChunkString, applicationConfigGenericSO);
        htmlChunkString = addEntry(htmlChunkString, LoginHtmlChunk.NAME);
        return htmlChunkString;
    }

}
