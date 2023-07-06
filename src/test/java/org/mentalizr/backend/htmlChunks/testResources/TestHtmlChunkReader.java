package org.mentalizr.backend.htmlChunks.testResources;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.backend.htmlChunks.definitions.ImprintHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.PolicyConsentHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;
import org.mentalizr.backend.htmlChunks.reader.HtmlChunkReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestHtmlChunkReader implements HtmlChunkReader {

    public static final String POLICY =
            "<html>\n" +
                    "    <h1>This is a policy</h2>\n" +
                    "</html>\n";

    public static final String IMPRINT =
            "<html>\n" +
                    "    <h1>This is a imprint</h2>\n" +
                    "</html>\n";

    @Override
    public String asString(HtmlChunk htmlChunk) {
        if (htmlChunk instanceof InternalHtmlChunk) {
            return fromWebAppResource(htmlChunk);
        } else if (htmlChunk instanceof PolicyConsentHtmlChunk) {
            return POLICY;
        } else if (htmlChunk instanceof ImprintHtmlChunk) {
            return IMPRINT;
        }
        throw new RuntimeException("Not implemented.");
    }


    public String fromWebAppResource(HtmlChunk htmlChunk) {
        InternalHtmlChunk internalHtmlChunk = (InternalHtmlChunk) htmlChunk;
        String fileName = internalHtmlChunk.getFileName();
        if (fileName.startsWith("/") && fileName.length() > 1)
            fileName = fileName.substring(1);
        Path path = FileUtils.getWorkingDir().resolve("src/main/webapp").resolve(fileName);
        if (!FileUtils.isExistingRegularFile(path))
            throw new RuntimeException("Chunk file [" + path.toAbsolutePath() + "] not found.");
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
