package org.mentalizr.backend.htmlChunks.definitions.hierarchy;

import org.mentalizr.backend.htmlChunks.modifier.HtmlChunkModifier;
import org.mentalizr.backend.htmlChunks.modifier.NoopModifier;

import java.util.Objects;

public abstract class HtmlChunk {

    public abstract String getName();

    public abstract String getFileName();

    public HtmlChunkModifier getModifier() {
        return new NoopModifier();
    }

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
