package org.mentalizr.backend.htmlChunks.reader;

import org.mentalizr.backend.htmlChunks.types.HtmlChunk;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class InternalHtmlChunkReader extends HtmlChunkReader {

    public InternalHtmlChunkReader(HtmlChunk htmlChunk, ServletContext servletContext) {
        super(htmlChunk, servletContext);
    }

    @Override
    public String asString() {
        String fileName = this.htmlChunk.getFileName();
        try (InputStream inputStream = servletContext.getResourceAsStream(fileName)) {
            if (inputStream == null)
                throw new RuntimeException("HtmlChunk not found in web application: [" + fileName + "].");
            return toStringWithNormalizedLineDelimiter(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not read HtmlChunk [" + fileName + "] from WebApp Resources: "
                    + e.getMessage(), e);
        }
    }

    private static String toStringWithNormalizedLineDelimiter(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader
                     = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

}
