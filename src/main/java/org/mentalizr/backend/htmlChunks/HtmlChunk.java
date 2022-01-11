package org.mentalizr.backend.htmlChunks;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class HtmlChunk {

    protected final String chunk;

    public HtmlChunk(ServletContext servletContext) throws IOException {
        try (InputStream inputStream = servletContext.getResourceAsStream(getFileName())) {
            if (inputStream == null)
                throw new RuntimeException("HtmlChunk not found in web application: [" + getFileName() + "].");
            String chunkWork = toStringWithNormalizedLineDelimiter(inputStream);
            this.chunk = modifyChunk(chunkWork);
        }
    }

    public String asString() {
        return this.chunk;
    }

    public InputStream asInputStream() throws IOException {
        return fromString(chunk);
    }

    public abstract String getName();

    public abstract String getFileName();

    public abstract String modifyChunk(String chunk);

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
