package org.mentalizr.backend.htmlChunks.modifier;

import org.mentalizr.backend.htmlChunks.definitions.LoginVoucherHtmlChunk;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class InitVoucherHtmlChunkModifier extends InitHtmlChunkModifier implements HtmlChunkModifier {

    @Override
    public String modify(String htmlChunkString, ApplicationConfigGenericSO applicationConfigGenericSO) {
        htmlChunkString = super.modify(htmlChunkString, applicationConfigGenericSO);
        htmlChunkString = addEntry(htmlChunkString, LoginVoucherHtmlChunk.NAME);
        return htmlChunkString;
    }

}
