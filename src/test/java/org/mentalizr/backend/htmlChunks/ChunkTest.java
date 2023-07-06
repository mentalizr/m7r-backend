package org.mentalizr.backend.htmlChunks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mentalizr.backend.htmlChunks.definitions.*;
import org.mentalizr.backend.htmlChunks.testResources.ApplicationConfigGenericSOFactory;
import org.mentalizr.backend.htmlChunks.testResources.TestHtmlChunkReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChunkTest {

    private static HtmlChunkCache htmlChunkCache;
    private static final boolean outputActualChunk = true;

    @BeforeAll
    public static void setUp() {
        htmlChunkCache = new HtmlChunkCache(
                new TestHtmlChunkReader(),
                ApplicationConfigGenericSOFactory.create(),
                null
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

    @Test
    public void loginChunk() throws IOException {
        String expected = Files.readString(Paths.get("src/test/resources/htmlChunks/expected/login.chunk.begin.html.expected"));
        String actual = htmlChunkCache.getChunkAsString(LoginHtmlChunk.NAME);
        if (outputActualChunk) System.out.println(actual);
        Assertions.assertTrue(actual.startsWith(expected));
    }

    @Test
    public void loginVoucherChunk() throws IOException {
        String expected = Files.readString(Paths.get("src/test/resources/htmlChunks/expected/login.chunk.begin.html.expected"));
        String actual = htmlChunkCache.getChunkAsString(LoginVoucherHtmlChunk.NAME);
        if (outputActualChunk) System.out.println(actual);
        Assertions.assertTrue(actual.startsWith(expected));
    }

    @Test
    public void patientChunk() throws IOException {
        String expected = Files.readString(Paths.get("src/main/webapp/WEB-INF/patient.chunk.html"));
        String actual = htmlChunkCache.getChunkAsString(PatientHtmlChunk.NAME);
        if (outputActualChunk) System.out.println(actual);
        Assertions.assertTrue(actual.startsWith(expected));
    }

    @Test
    public void therapistChunk() throws IOException {
        String expected = Files.readString(Paths.get("src/main/webapp/WEB-INF/therapist.chunk.html"));
        String actual = htmlChunkCache.getChunkAsString(TherapistHtmlChunk.NAME);
        if (outputActualChunk) System.out.println(actual);
        Assertions.assertTrue(actual.startsWith(expected));
    }

    @Test
    public void policyChunk() {
        String expected = TestHtmlChunkReader.POLICY;
        String actual = htmlChunkCache.getChunkAsString(PolicyHtmlChunk.NAME);
        if (outputActualChunk) System.out.println(actual);
        Assertions.assertTrue(actual.startsWith(expected));
    }

    @Test
    public void imprintChunk() {
        String expected = TestHtmlChunkReader.IMPRINT;
        String actual = htmlChunkCache.getChunkAsString(ImprintHtmlChunk.NAME);
        if (outputActualChunk) System.out.println(actual);
        Assertions.assertTrue(actual.startsWith(expected));
    }

}
