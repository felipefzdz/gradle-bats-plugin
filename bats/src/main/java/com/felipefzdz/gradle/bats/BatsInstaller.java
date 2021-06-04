package com.felipefzdz.gradle.bats;

import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.felipefzdz.gradle.bats.Shell.run;
import static java.util.Arrays.asList;

public class BatsInstaller {

    private static final Map<String, List<String>> INSTALLER_COMMANDS = new HashMap<String, List<String>>() {{
        put("brew", asList(
                "brew tap kaos/shell",
                "brew install bats-assert",
                "brew install bats-file",
                "brew install bats-mock",
                "brew install bats-support",
                "brew install bats-core"));
        put("npm", asList(
                "npm install -g bats",
                "npm install -g https://github.com/ztombol/bats-assert",
                "npm install -g https://github.com/ztombol/bats-file",
                "npm install -g https://github.com/jasonkarns/bats-mock",
                "npm install -g https://github.com/ztombol/bats-support"));
    }};

    public static void maybeInstallBats(String installer, File projectDir, Logger logger) throws IOException, InterruptedException {
        if (installer.isEmpty()) {
            return;
        }

        if ("brew".equals(installer)) {
            final String commandBats = run("command bats", projectDir, logger);
            logger.debug("command bats returned: " + commandBats);
            if (!commandBats.contains("bats: command not found")) {
                logger.debug("Bats is already installed. Skipping installation.");
                return;
            }
        }

        List<String> installerCommands = Optional.ofNullable(INSTALLER_COMMANDS.get(installer))
                .orElseThrow(() -> new IllegalArgumentException("Installer " + installer + " is not supported"));
        for (String installerCommand : installerCommands) {
            run(installerCommand, projectDir, logger);
        }
    }
}
