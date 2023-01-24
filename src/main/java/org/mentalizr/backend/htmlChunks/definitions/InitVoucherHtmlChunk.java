package org.mentalizr.backend.htmlChunks.definitions;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.modifier.InitVoucherHtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

public class InitVoucherHtmlChunk extends InternalHtmlChunk {

    public static final String NAME = "INIT_VOUCHER";

    public InitVoucherHtmlChunk(HtmlChunkReader htmlChunkReader) {
        super(htmlChunkReader);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getFileName() {
        return "/WEB-INF/init.html";
    }

    @Override
    public InitHtmlChunkModifier getModifier() {
        return new InitVoucherHtmlChunkModifier();
    }

}
