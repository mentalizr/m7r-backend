package org.mentalizr.backend.htmlChunks.modifier;

import org.mentalizr.backend.exceptions.M7rInconsistencyException;
import org.mentalizr.backend.htmlChunks.HtmlChunkManager;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHtmlChunkModifier implements HtmlChunkModifier {

    private static final String PLACEHOLDER = "_DUMMY_LOGO_";

    @Override
    public String modify(String htmlChunkString, ApplicationConfigGenericSO applicationConfigGenericSO) {
        if (!htmlChunkString.contains(PLACEHOLDER))
            throw new M7rInconsistencyException("HtmlChunk Login/LoginVoucher does not contain logo placeholder [" + PLACEHOLDER + "].");

        String logo = applicationConfigGenericSO.getLogo();
        return htmlChunkString.replace("_DUMMY_LOGO_", logo);
    }

}
