package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;
import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public abstract class InitHtmlChunkProducer extends HtmlChunkProducer {

    public InitHtmlChunkProducer(HtmlChunk htmlChunk, ApplicationConfigGenericSO applicationConfigGenericSO) {
        super(htmlChunk, applicationConfigGenericSO);
    }

//    @Override
//    public void modify() {
//        InitHtmlChunkModifier initHtmlChunkModifier = (InitHtmlChunkModifier) this.htmlChunkModifier;
//        addTitle(initHtmlChunkModifier);
//        addEntry(initHtmlChunkModifier);
//    }
//
//    private void addTitle(InitHtmlChunkModifier initHtmlChunkModifier) {
//        String title = this.applicationConfigGenericSO.getTitle();
//        initHtmlChunkModifier.addTitle(title);
//    }
//
//    public abstract void addEntry(InitHtmlChunkModifier initHtmlChunkModifier);

}
