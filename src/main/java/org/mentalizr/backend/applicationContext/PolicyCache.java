package org.mentalizr.backend.applicationContext;

import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.utils.io.nio2.FileUtils;
import org.mentalizr.backend.config.instance.InstanceConfiguration;
import org.mentalizr.commons.paths.container.TomcatContainerM7rHostConfigDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PolicyCache {

    private final String policyHtml;

    public static PolicyCache createInstance(InstanceConfiguration instanceConfiguration) {
        Path dir = new TomcatContainerM7rHostConfigDir().asPath();
        String version = instanceConfiguration.getApplicationConfigGenericSO().getPolicyVersion();
        return new PolicyCache(dir, version);
    }

    public PolicyCache(Path policyDir, String version) {
        if (Strings.containsWhitespace(version))
            throw new InitializationException("Policy version string contains whitespace: [" + version + "].");

        Path policyHtmlFile = policyDir.resolve("policy-" + version + ".html");
        if (!FileUtils.isExistingRegularFile(policyHtmlFile))
            throw new InitializationException("Policy file not found. [" + policyHtmlFile.toAbsolutePath() + "].");

        policyHtml = readFileToString(policyHtmlFile);
    }

    public String getPolicyHtml() {
        return policyHtml;
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
