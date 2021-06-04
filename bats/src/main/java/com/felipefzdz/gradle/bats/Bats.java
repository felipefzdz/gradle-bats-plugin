package com.felipefzdz.gradle.bats;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.*;

import javax.inject.Inject;
import java.io.File;

@CacheableTask
public class Bats extends ConventionTask {

    private FileCollection sources;
    private boolean useDocker = true;
    private String batsBinary;
    private String installer;
    private File workingDir;


    @Inject
    protected ObjectFactory getObjectFactory() {
        throw new UnsupportedOperationException();
    }

    @TaskAction
    public void run() {
        BatsInvoker.invoke(this);
    }

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    @Optional
    public FileCollection getSources() {
        return sources;
    }

    public void setSources(FileCollection sources) {
        this.sources = sources;
    }

    @Input
    public boolean isUseDocker() {
        return useDocker;
    }

    public void setUseDocker(boolean useDocker) {
        this.useDocker = useDocker;
    }

    @Input
    public String getBatsBinary() {
        return batsBinary;
    }

    public void setBatsBinary(String batsBinary) {
        this.batsBinary = batsBinary;
    }

    @Input
    public String getInstaller() {
        return installer;
    }

    public void setInstaller(String installer) {
        this.installer = installer;
    }

    @Internal
    public File getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(File workingDir) {
        this.workingDir = workingDir;
    }

}
