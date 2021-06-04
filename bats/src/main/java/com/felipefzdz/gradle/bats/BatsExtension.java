package com.felipefzdz.gradle.bats;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.quality.CodeQualityExtension;

import java.io.File;

public class BatsExtension extends CodeQualityExtension {

    private final Project project;

    private FileCollection sources;
    private boolean useDocker = true;
    private String batsBinary = "/usr/local/bin/bats";
    private String installer = "";
    private File workingDir;

    public BatsExtension(Project project) {
        this.project = project;
        this.workingDir = project.getProjectDir();
    }

    public FileCollection getSources() {
        return sources;
    }

    public void setSources(FileCollection sources) {
        this.sources = sources;
    }

    public boolean getUseDocker() {
        return useDocker;
    }

    public void setUseDocker(boolean useDocker) {
        this.useDocker = useDocker;
    }

    public boolean isUseDocker() {
        return useDocker;
    }

    public String getBatsBinary() {
        return batsBinary;
    }

    public void setBatsBinary(String batsBinary) {
        this.batsBinary = batsBinary;
    }

    public String getInstaller() {
        return installer;
    }

    public void setInstaller(String installer) {
        this.installer = installer;
    }

    public File getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(File workingDir) {
        this.workingDir = workingDir;
    }
}
