package org.mentalizr.backend.applicationContext;

import de.arthurpicht.utils.core.strings.Strings;
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

public class PolicyCache {

    private static final Logger logger = LoggerFactory.getLogger(PolicyCache.class);
    private final String policyFileName;
    private final String policyHtml;

    public static PolicyCache createInstance(InstanceConfiguration instanceConfiguration) {
        Path dir = new M7rHostConfigDir().asPath();
        String version = instanceConfiguration.getApplicationConfigGenericSO().getPolicyVersion();
        return new PolicyCache(dir, version);
    }

    public PolicyCache(Path policyDir, String version) {
        if (Strings.containsWhitespace(version))
            throw new InitializationException("Policy version string contains whitespace: [" + version + "].");

        this.policyFileName = "policy-" + version + ".html";
        Path policyHtmlFile = policyDir.resolve(this.policyFileName);
        if (!FileUtils.isExistingRegularFile(policyHtmlFile))
            throw new InitializationException("Policy file not found. [" + policyHtmlFile.toAbsolutePath() + "].");

        logger.info("Load policy file [" + policyHtmlFile.toAbsolutePath() + "].");

        policyHtml = readFileToString(policyHtmlFile);
    }

    public String getPolicyFileName() {
        return this.policyFileName;
    }

    public String getPolicyHtml() {
        return this.policyHtml;
    }

    private String readFileToString(Path file) {
        Stream<String> lines;
        try {
            lines = Files.lines(file);
        } catch (IOException e) {
            throw new InitializationException("Could not read policy file [" + file.toAbsolutePath() + "].", e);
        }
        String string = lines.collect(Collectors.joining("\n"));
        lines.close();
        return string;
    }
}
