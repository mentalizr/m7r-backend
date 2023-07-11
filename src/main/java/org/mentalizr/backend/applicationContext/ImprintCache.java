package org.mentalizr.backend.applicationContext;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.commons.paths.host.hostDir.M7rHostConfigDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImprintCache {

    private static final Logger logger = LoggerFactory.getLogger(ImprintCache.class);
    private final String imprintHtml;

    public static ImprintCache createInstance(InstanceConfiguration instanceConfiguration) {
        Path dir = new M7rHostConfigDir().asPath();
        return new ImprintCache(dir);
    }

    public ImprintCache(Path policyDir) {
        Path imprintHtmlFile = policyDir.resolve("imprint.html");
        if (!FileUtils.isExistingRegularFile(imprintHtmlFile))
            throw new InitializationException("Imprint file not found. [" + imprintHtmlFile.toAbsolutePath() + "].");

        logger.info("Load imprint file [" + imprintHtmlFile.toAbsolutePath() + "].");

        imprintHtml = readFileToString(imprintHtmlFile);
    }

    public String getImprintHtml() {
        return imprintHtml;
    }

    private String readFileToString(Path file) {
        Stream<String> lines;
        try {
            lines = Files.lines(file);
        } catch (IOException e) {
            throw new InitializationException("Could not read imprint file [" + file.toAbsolutePath() + "].", e);
        }
        String string = lines.collect(Collectors.joining("\n"));
        lines.close();
        return string;
    }

}
