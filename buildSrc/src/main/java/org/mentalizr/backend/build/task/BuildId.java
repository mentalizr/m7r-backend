package org.mentalizr.backend.build.task;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.gradle.api.GradleException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BuildId {

    private static final String BUILD_ID = "src/main/resources/buildId.txt";

    // Dummy

    public static String read() {
        Path buildIdPath = Paths.get(BUILD_ID);
        if (!FileUtils.isExistingRegularFile(buildIdPath))
            throw new GradleException("File not found: [" + buildIdPath.toAbsolutePath() + "].");

        try {
            return Files.readString(buildIdPath);
        } catch (IOException e) {
            throw new GradleException("Exception on reading file: [" + buildIdPath.toAbsolutePath() + "].");
        }
    }

}
