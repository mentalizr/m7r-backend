package org.mentalizr.backend.build.task;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

public class CopyStaticResourcesFromFrontendTask extends DefaultTask {

    private static final String SOURCE_DIR = "../m7r-frontend-project/html";
    private static final String DESTINATION_DIR = "src/main/webapp/WEB-INF";

    @TaskAction
    public void run() {
        List<Path> sourceFiles = getSourceFiles();
        for (Path path : sourceFiles) {
            copyFile(path);
        }
    }

    private List<Path> getSourceFiles() {
        Path sourceDir = Paths.get(SOURCE_DIR);
        List<Path> sourceFiles;
        try {
            sourceFiles = FileUtils.getContainingFiles(sourceDir);
        } catch (IOException e) {
            throw new GradleException("Could not read [" + sourceDir.toAbsolutePath() + "]: " + e.getMessage(), e);
        }
        return sourceFiles.stream()
                .filter(p -> !p.getFileName().toString().equals("init.html"))
                .collect(Collectors.toList());
    }

    private void copyFile(Path filePath) {
        if (!FileUtils.isExistingRegularFile(filePath))
            throw new GradleException("File not found: [" + filePath.toAbsolutePath() + "].");

        Path destination = Paths.get(DESTINATION_DIR).resolve(filePath.getFileName());
        try {
            Files.copy(filePath, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new GradleException("Could not copy File [" + filePath.getFileName() + "] to " +
                    "[" + destination.toAbsolutePath() + "]: " + e.getMessage(), e);
        }
    }

}
