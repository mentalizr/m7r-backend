package org.mentalizr.backend.htmlChunks.reader;

import org.mentalizr.backend.applicationContext.PolicyCache;
import org.mentalizr.backend.exceptions.M7rInconsistencyException;
import org.mentalizr.backend.exceptions.M7rInfrastructureRuntimeException;
import org.mentalizr.backend.htmlChunks.definitions.hierarchy.HtmlChunk;
import org.mentalizr.backend.htmlChunks.definitions.hierarchy.InternalHtmlChunk;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ProductionHtmlChunkReader implements HtmlChunkReader {

    private final ServletContext servletContext;
    private final PolicyCache policyCache;

    public ProductionHtmlChunkReader(ServletContext servletContext, PolicyCache policyCache) {
        this.servletContext = servletContext;
        this.policyCache = policyCache;
    }

    @Override
    public String fromWebAppResource(HtmlChunk htmlChunk) {
        InternalHtmlChunk internalHtmlChunk = (InternalHtmlChunk) htmlChunk;
        String fileName = internalHtmlChunk.getFileName();
        try (InputStream inputStream = servletContext.getResourceAsStream(fileName)) {
            if (inputStream == null)
                throw new M7rInconsistencyException("HtmlChunk not found in web application: [" + fileName + "].");
            return toStringWithNormalizedLineDelimiter(inputStream);
        } catch (IOException e) {
            throw new M7rInfrastructureRuntimeException(
                    "Could not read HtmlChunk [" + fileName + "] from WebApp Resources: "
                    + e.getMessage(), e);
        }
    }

    @Override
    public String fromPolicyConfiguration() {
        return this.policyCache.getPolicyHtml();
    }

    private static String toStringWithNormalizedLineDelimiter(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader
                     = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

}
