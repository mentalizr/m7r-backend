package org.mentalizr.backend.htmlChunks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mentalizr.backend.htmlChunks.definitions.InitLoginHtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.InitVoucherHtmlChunk;
import org.mentalizr.backend.htmlChunks.testResources.ApplicationConfigGenericSOFactory;
import org.mentalizr.backend.htmlChunks.testResources.TestHtmlChunkReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChunkTest {

    private static HtmlChunkCache htmlChunkCache;

    @BeforeAll
    public static void setUp() {
        htmlChunkCache = new HtmlChunkCache(
                new TestHtmlChunkReader(),
                ApplicationConfigGenericSOFactory.create()
        );
    }

    @Test
    public void initLoginChunk() throws IOException {
        String expected = Files.readString(Paths.get("src/test/resources/htmlChunks/expected/init.login.chunk.html.expected"));
        String actual = htmlChunkCache.getChunkAsString(InitLoginHtmlChunk.NAME);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void initVoucherChunk() throws IOException {
        String expected = Files.readString(Paths.get("src/test/resources/htmlChunks/expected/init.voucher.chunk.html.expected"));
        String actual = htmlChunkCache.getChunkAsString(InitVoucherHtmlChunk.NAME);

        Assertions.assertEquals(expected, actual);
    }

    


}
