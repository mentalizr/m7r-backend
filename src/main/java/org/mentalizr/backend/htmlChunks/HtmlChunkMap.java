package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.exceptions.M7rInconsistencyException;

import java.util.HashMap;
import java.util.Map;

public class HtmlChunkMap {

    private final Map<String, String> chunkStringMap;

    public HtmlChunkMap() {
        this.chunkStringMap = new HashMap<>();
    }

    public void put(String name, String htmlChunkString) {
        if (this.chunkStringMap.containsKey(name))
            throw new M7rInconsistencyException("Chunk is already contained: [" + name + "].");

        this.chunkStringMap.put(name, htmlChunkString);
    }

    public boolean contains(String name) {
        return this.chunkStringMap.containsKey(name);
    }

    public String getChunkAsString(String name) {
        assertExistence(name);
        return this.chunkStringMap.get(name);
    }

    private void assertExistence(String name) {
        if (!this.chunkStringMap.containsKey(name))
            throw new M7rInconsistencyException("Unknown chunk: [" + name + "].");
    }

}
