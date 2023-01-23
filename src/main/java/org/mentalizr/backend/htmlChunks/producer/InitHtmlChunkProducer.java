package org.mentalizr.backend.htmlChunks.producer;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.backend.htmlChunks.definitions.InitLoginHtmlChunk;
import org.mentalizr.backend.htmlChunks.modifier.InitHtmlChunkModifier;

public abstract class InitHtmlChunkProducer extends HtmlChunkProducer {

    public InitHtmlChunkProducer() {
        super(InitLoginHtmlChunk.NAME);
    }

    @Override
    public void modify() {
        InitHtmlChunkModifier initHtmlChunkModifier = (InitHtmlChunkModifier) this.htmlChunkModifier;
        addTitle(initHtmlChunkModifier);
        addEntry(initHtmlChunkModifier);
    }

    private void addTitle(InitHtmlChunkModifier initHtmlChunkModifier) {
        InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
        String title = instanceConfiguration.getApplicationConfigGenericSO().getTitle();
        initHtmlChunkModifier.addTitle(title);
    }

    public abstract void addEntry(InitHtmlChunkModifier initHtmlChunkModifier);

}
