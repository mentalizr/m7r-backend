package org.mentalizr.backend.htmlChunks;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HtmlChunkRegistry {

    private final Map<String, HtmlChunk> chunkMap;

    public HtmlChunkRegistry(ServletContext servletContext) throws IOException {
        this.chunkMap = new HashMap<>();
        putHtmlChunk(new HtmlChunkInit(servletContext));
        putHtmlChunk(new HtmlChunkLogin(servletContext));
        putHtmlChunk(new HtmlChunkLoginVoucher(servletContext));
        putHtmlChunk(new HtmlChunkPolicy(servletContext));
        putHtmlChunk(new HtmlChunkPatient(servletContext));
        putHtmlChunk(new HtmlChunkTherapist(servletContext));
    }

    private void putHtmlChunk(HtmlChunk htmlChunk) {
        this.chunkMap.put(htmlChunk.getName(), htmlChunk);
    }

    public HtmlChunk getChunk(String name) {
        if (this.chunkMap.containsKey(name)) {
            return this.chunkMap.get(name);
        }
        throw new RuntimeException("Unknown HtmlChunk: [" + name + "].");
    }

}
