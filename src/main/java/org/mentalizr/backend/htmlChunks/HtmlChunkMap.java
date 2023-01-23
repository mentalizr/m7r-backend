package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;
import org.mentalizr.backend.htmlChunks.types.HtmlChunk;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public class HtmlChunkMap {

    private final Map<String, HtmlChunk> chunkMap;
    private final Map<String, String> chunkStringMap;

    public HtmlChunkMap() {
        this.chunkMap = new HashMap<>();
        this.chunkStringMap = new HashMap<>();
    }

    public void put(HtmlChunk htmlChunk, ServletContext servletContext) {
        String name = htmlChunk.getName();
        if (this.chunkMap.containsKey(name))
            throw new IllegalStateException("Chunk is already contained: [" + name + "].");

        this.chunkMap.put(name, htmlChunk);

        HtmlChunkReader htmlChunkReader = htmlChunk.getReader(servletContext);
        String htmlChunkString = htmlChunkReader.asString();
        this.chunkStringMap.put(name, htmlChunkString);
    }

    public boolean contains(String name) {
        return this.chunkMap.containsKey(name);
    }

    public HtmlChunk getHtmlChunk(String name) {
        assertExistence(name);
        return this.chunkMap.get(name);
    }

    public String getChunkAsString(String name) {
        assertExistence(name);
        return this.chunkStringMap.get(name);
    }

    private void assertExistence(String name) {
        if (!this.chunkMap.containsKey(name))
            throw new IllegalArgumentException("Unknown chunk: [" + name + "].");
    }

}
