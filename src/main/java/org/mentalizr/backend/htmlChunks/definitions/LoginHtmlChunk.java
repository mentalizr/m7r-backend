package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.LoginHtmlChunkModifier;

public class LoginHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "LOGIN";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/login.chunk.html";
    }

    @Override
    public LoginHtmlChunkModifier getModifier() {
        return new LoginHtmlChunkModifier();
    }

}
