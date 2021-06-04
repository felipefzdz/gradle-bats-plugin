package com.felipefzdz.gradle.bats;

import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.felipefzdz.gradle.bats.Shell.run;

public class BatsInvoker {

    public static void invoke(Bats task) {
        maybeInstallBats(task);
        String output = runBatsWithTaps13Format(task);
        if (output.contains("not ok")) {
            throw new GradleException(output);
        } else {
            task.getLogger().info(output);
        }
    }

    private static void maybeInstallBats(Bats task) {
        try {
            if (!task.isUseDocker()) {
                BatsInstaller.maybeInstallBats(task.getInstaller(), task.getWorkingDir(), task.getLogger());
            }
        } catch (IOException | InterruptedException e) {
            throw new GradleException("Error installing Bats ", e);
        }
    }

    private static String runBatsWithTaps13Format(Bats task) {
        try {
            return String.join("\n\n", runBats(task, "tap13"));
        } catch (IOException | InterruptedException e) {
            throw new GradleException("Error while handling Bats pretty report", e);
        }
    }

    public static List<String> runBats(Bats task, String format) throws IOException, InterruptedException {
        if (isNullOrEmpty(task.getSources())) {
            return Collections.singletonList("No files specified.");
        } else {
            task.getLogger().debug("source dirs: " + task.getSources());
        }
        return runBats(task, format, task.getSources().getFiles());
    }

    private static boolean isNullOrEmpty(FileCollection collection) {
        return collection == null || collection.isEmpty();
    }

    private static List<String> runBats(Bats task, String format, Set<File> sourceFiles) {
        return sourceFiles.stream().map(f -> {
            try {
                final List<String> command = new ArrayList<>();
                if (task.isUseDocker()) {
                    command.add("docker");
                    command.add("run");
                    command.add("--rm");

                    command.add("-v");
                    command.add(f.getCanonicalPath() + ":" + f.getCanonicalPath());
                    command.add("-w");
                    command.add(f.getCanonicalPath());
                    command.add("felipefzdz/bats:1.3.0");
                } else {
                    command.add(task.getBatsBinary());
                }
                command.add("-F");
                command.add(format);
                command.add(f.getCanonicalPath());
                task.getLogger().debug("Command to run Bats: " + String.join(" ", command));
                return run(command, task.getWorkingDir(), task.getLogger()).trim();
            } catch (IOException | InterruptedException e) {
                throw new GradleException(e.getMessage(), e);
            }
        }).collect(Collectors.toList());
    }
}
