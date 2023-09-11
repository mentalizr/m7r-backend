package org.mentalizr.backend.build.task;

import de.arthurpicht.utils.core.strings.StringSubstitutor;
import de.arthurpicht.utils.core.strings.StringSubstitutorConfiguration;
import de.arthurpicht.utils.io.nio2.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExpandInitHtmlTask extends DefaultTask {

    @TaskAction
    public void run() {
        Path initHtml = Paths.get("../m7r-frontend-project/html/init.html");
        if (!FileUtils.isExistingRegularFile(initHtml))
            throw new GradleException("File [" + initHtml.toAbsolutePath() + "] not found.");

        String initHtmlString = getInitHtmlAsString(initHtml);
        StringSubstitutorConfiguration stringSubstitutorConfiguration = getStringSubstitutionConfiguration();
        String initHtmlExtendedString = StringSubstitutor.substitute(initHtmlString, stringSubstitutorConfiguration);
        writeExtendedFile(initHtmlExtendedString);
    }

    private static String getInitHtmlAsString(Path initHtml) {
        try {
            return Files.readString(initHtml);
        } catch (IOException e) {
            throw new GradleException("Exception reading [" + initHtml.toAbsolutePath() + "]: " + e.getMessage(), e);
        }
    }

    private static StringSubstitutorConfiguration getStringSubstitutionConfiguration() {
        return new StringSubstitutorConfiguration.Builder()
                .withPre("${{")
                .withPost("}}")
                .withSubstitution("buildId", BuildId.read())
                .build();
    }

    private static void writeExtendedFile(String initHtmlExtendedString) {
        Path initHtmlExtendedFile = Paths.get("src/main/webapp/WEB-INF/init.html");
        try {
            Files.writeString(initHtmlExtendedFile, initHtmlExtendedString);
        } catch (IOException e) {
            throw new GradleException("Exception writing [" + initHtmlExtendedFile.toAbsolutePath() + "]: "
                    + e.getMessage(), e);
        }
    }

}
