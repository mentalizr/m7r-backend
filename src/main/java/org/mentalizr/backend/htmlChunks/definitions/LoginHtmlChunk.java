package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.LoginHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

public class LoginHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "LOGIN";

    public LoginHtmlChunk(HtmlChunkReader htmlChunkReader) {
        super(htmlChunkReader);
    }

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
