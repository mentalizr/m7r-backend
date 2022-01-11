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

//    public String getChunkAsString(HtmlChunk htmlChunk) throws IOException {
//
//        String htmlChunkName = htmlChunk.getName();
//        if (this.chunkMap.containsKey(htmlChunkName)) {
//            return this.chunkMap.get(htmlChunkName);
//        }
//
//        this.chunkMap.put(htmlChunkName, htmlChunk.asString());
//        return htmlChunk.asString();
//    }

//    public InputStream getChunk(HtmlChunk htmlChunk) throws IOException {
//        String chunk = getChunkAsString(htmlChunk);
//        return fromString(chunk);
//    }

//    private static String toStringWithNormalizedLineDelimiter(InputStream inputStream, Charset charset, CharSequence delimiter) throws IOException {
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
//            return bufferedReader.lines().collect(Collectors.joining(delimiter));
//        }
//    }
//
//    private static String toStringWithNormalizedLineDelimiter(InputStream inputStream) throws IOException {
//        return toStringWithNormalizedLineDelimiter(inputStream, StandardCharsets.UTF_8, "\n");
//    }
//
//    private static InputStream fromString(String string, Charset charset) {
//        return new ByteArrayInputStream(string.getBytes(charset));
//    }
//
//    private static InputStream fromString(String string) {
//        return fromString(string, StandardCharsets.UTF_8);
//    }

}
