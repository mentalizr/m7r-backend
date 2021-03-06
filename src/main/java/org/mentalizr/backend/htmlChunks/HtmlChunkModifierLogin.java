package org.mentalizr.backend.htmlChunks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlChunkModifierLogin extends HtmlChunkModifier {

    private static final Logger logger = LoggerFactory.getLogger(HtmlChunkManager.class);
    private static final String PLACEHOLDER = "_DUMMY_LOGO_";

    public HtmlChunkModifierLogin(String chunk) {
        super(chunk);
    }

    public void addLogo(String logo) {

        if (!this.chunk.contains(PLACEHOLDER)) {
            logger.warn("HtmlChunk Login/LoginVoucher does not contain logo placeholder [" + PLACEHOLDER + "].");
            return;
        }

        this.chunk = this.chunk.replace("_DUMMY_LOGO_", logo);
    }

}
