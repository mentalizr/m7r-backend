package org.mentalizr.backend.htmlChunks;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HtmlChunkCache {

    private final ServletContext servletContext;
    private final Map<String, String> chunkMap;

    public HtmlChunkCache(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.chunkMap = new HashMap<>();
    }

    public String getChunkAsString(HtmlChunk htmlChunk) throws IOException {

        String htmlChunkName = htmlChunk.name();
        if (this.chunkMap.containsKey(htmlChunkName)) {
            return this.chunkMap.get(htmlChunkName);
//            return fromString(chunk);
        }

        String fileName = htmlChunk.getFileName();
        try (InputStream inputStream = servletContext.getResourceAsStream(fileName)) {

            if (inputStream == null) throw new RuntimeException("Resource not found in web application: " + fileName);

            String chunk = toStringWithNormalizedLineDelimiter(inputStream);
            this.chunkMap.put(htmlChunkName, chunk);
//            return fromString(chunk);
            return chunk;
        }

    }

    public InputStream getChunk(HtmlChunk htmlChunk) throws IOException {

        String chunk = getChunkAsString(htmlChunk);
        return fromString(chunk);

//        String htmlChunkName = htmlChunk.name();
//
//        if (this.chunkMap.containsKey(htmlChunkName)) {
//            String chunk = this.chunkMap.get(htmlChunkName);
//            return fromString(chunk);
//        }
//
//        try (InputStream inputStream = servletContext.getResourceAsStream(htmlChunkName)) {
//            String chunk = toStringWithNormalizedLineDelimiter(inputStream);
//            this.chunkMap.put(htmlChunkName, chunk);
//            return fromString(chunk);
//        }
    }

    private static String toStringWithNormalizedLineDelimiter(InputStream inputStream, Charset charset, CharSequence delimiter) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            return bufferedReader.lines().collect(Collectors.joining(delimiter));
        }
    }

    private static String toStringWithNormalizedLineDelimiter(InputStream inputStream) throws IOException {
        return toStringWithNormalizedLineDelimiter(inputStream, StandardCharsets.UTF_8, "\n");
    }

    private static InputStream fromString(String string, Charset charset) {
        return new ByteArrayInputStream(string.getBytes(charset));
    }

    private static InputStream fromString(String string) {
        return fromString(string, StandardCharsets.UTF_8);
    }

}
