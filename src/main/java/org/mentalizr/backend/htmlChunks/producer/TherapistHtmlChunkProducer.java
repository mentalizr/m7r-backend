package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.TherapistHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public class TherapistHtmlChunkProducer extends HtmlChunkProducer {

    public TherapistHtmlChunkProducer(HtmlChunkReader htmlChunkReader, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(new TherapistHtmlChunk(), htmlChunkReader, applicationConfigGenericSO);
    }

}
