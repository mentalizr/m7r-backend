package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.LoginVoucherHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.PolicyHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class PolicyHtmlChunkProducer extends HtmlChunkProducer {

    public PolicyHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(new PolicyHtmlChunk(htmlChunkReader), applicationConfigGenericSO);
    }

//    @Override
//    public void modify() {
//    }

}
