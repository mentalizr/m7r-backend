package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.PatientHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class PatientHtmlChunkProducer extends HtmlChunkProducer {

    public PatientHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(new PatientHtmlChunk(htmlChunkReader), applicationConfigGenericSO);
    }

//    @Override
//    public void modify() {
//        // din
//    }

}
