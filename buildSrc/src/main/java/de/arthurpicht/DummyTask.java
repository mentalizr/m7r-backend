package de.arthurpicht;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class DummyTask extends DefaultTask {

    @TaskAction
    public void run() {
        System.out.println("This is the DummyTask running!");
    }

}
