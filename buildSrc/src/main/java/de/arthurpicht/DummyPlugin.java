package de.arthurpicht;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DummyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().create("dummy", DummyTask.class);
    }
}
