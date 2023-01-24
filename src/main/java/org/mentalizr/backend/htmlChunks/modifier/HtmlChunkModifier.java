package org.mentalizr.backend.htmlChunks.modifier;

import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public interface HtmlChunkModifier {

    public String modify(String htmlChunkString, ApplicationConfigGenericSO applicationConfigGenericSO);

//    protected String chunk;
//
//    public void setRawChunk(String chunk) {
//        this.chunk = chunk;
//    }
//
//    public String getModifiedChunk() {
//        return this.chunk;
//    }

}
