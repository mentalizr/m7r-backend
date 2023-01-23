package org.mentalizr.backend.htmlChunks.types;

import org.mentalizr.backend.htmlChunks.modifier.HtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

import javax.servlet.ServletContext;
import java.util.Objects;

public abstract class HtmlChunk {

    public abstract String getName();

    public abstract String getFileName();

    public abstract HtmlChunkReader getReader(ServletContext servletContext);

    public HtmlChunkModifier getModifier() {
        throw new IllegalStateException("Intentionally not implemented. Override if necessary.");
    }

    public abstract HtmlChunkProducer getProducer();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HtmlChunk htmlChunk = (HtmlChunk) o;
        return Objects.equals(getName(), htmlChunk.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
