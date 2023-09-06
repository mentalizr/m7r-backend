package de.arthurpicht;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ExpandInitHtmlTask extends DefaultTask {

    @TaskAction
    public void run() {
        System.out.println("This is the ExpandInitHtmlTask running!");

        Path initHtml = Paths.get("../m7r-frontend-project/html/init.html");
        if (!FileUtils.isExistingRegularFile(initHtml))
            throw new GradleException("File [" + initHtml.toAbsolutePath() + "] not found.");

        
    }

}
