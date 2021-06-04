package com.felipefzdz.gradle.bats;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.ConventionMapping;
import org.gradle.api.plugins.ReportingBasePlugin;

import java.io.File;
import java.util.concurrent.Callable;

public class BatsPlugin implements Plugin<Project> {

    protected BatsExtension extension;

    public void apply(Project project) {
        project.getPluginManager().apply(ReportingBasePlugin.class);
        extension = (BatsExtension) project.getExtensions().create("bats", BatsExtension.class, project);
        project.getTasks().register("bats", Bats.class);
        project.getTasks().withType(Bats.class).configureEach(task -> configureTask((Bats) task, project));
    }

    private void configureTask(Bats task, Project project) {
        configureTaskConventionMapping(task, project);
    }

    private void configureTaskConventionMapping(Bats task, Project project) {
        ConventionMapping taskMapping = task.getConventionMapping();
        taskMapping.map("sources", (Callable<FileCollection>) () -> extension.getSources());
        taskMapping.map("useDocker", (Callable<Boolean>) () -> extension.isUseDocker());
        taskMapping.map("batsBinary", (Callable<String>) () -> extension.getBatsBinary());
        taskMapping.map("installer", (Callable<String>) () -> extension.getInstaller());
        taskMapping.map("workingDir", (Callable<File>) () -> extension.getWorkingDir());
    }
}
