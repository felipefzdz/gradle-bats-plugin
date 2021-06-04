package com.felipefzdz.gradle.bats

import spock.lang.IgnoreIf

@IgnoreIf({ !os.macOs })
class BatsInstallerPluginFuncTest extends BaseInfraTest {
    boolean useDocker = false
    String batsBinary = "/usr/local/bin/bats"

    def "install bats by using the specified installer"() {
        given:
        uninstallBats()

        buildFile << """
bats {
    sources = files("${resources.absolutePath}/failure_tests")
    useDocker = $useDocker
    batsBinary = "$batsBinary"
    installer = "brew"
}
"""

        expect:
        runner().buildAndFail().getOutput().contains("not ok 1 can run our script")
    }

    def "skip installing bats if already present"() {
        given:
        uninstallBats()
        installBats()

        buildFile << """
bats {
    sources = files("${resources.absolutePath}/failure_tests")
    useDocker = $useDocker
    batsBinary = "$batsBinary"
    installer = "brew"
}
"""

        when:
        def output = runnerWithDebugLogging().buildAndFail().getOutput()

        then:
        output.contains("not ok 1 can run our script")
        output.contains("Bats is already installed. Skipping installation.")
    }

    def "skip installing bats if useDocker is true"() {
        given:
        uninstallBats()
        installBats()

        buildFile << """
bats {
    sources = files("${resources.absolutePath}/failure_tests")
    useDocker = true
    batsBinary = "$batsBinary"
    installer = "brew"
}
"""

        when:
        def output = runnerWithDebugLogging().buildAndFail().getOutput()

        then:
        output.contains("not ok 1 can run our script")
        !output.contains("command bats returned:")
    }

    static def uninstallBats() {
        ["brew", "uninstall", "bats-core"].execute().waitForProcessOutput()
        ["brew", "uninstall", "bats-assert"].execute().waitForProcessOutput()
        ["brew", "uninstall", "bats-file"].execute().waitForProcessOutput()
        ["brew", "uninstall", "bats-mock"].execute().waitForProcessOutput()
        ["brew", "uninstall", "bats-support"].execute().waitForProcessOutput()
    }

    static def installBats() {
        ["brew", "install", "bats-core"].execute().waitForProcessOutput()
        ["brew", "install", "bats-assert"].execute().waitForProcessOutput()
        ["brew", "install", "bats-file"].execute().waitForProcessOutput()
        ["brew", "install", "bats-mock"].execute().waitForProcessOutput()
        ["brew", "install", "bats-support"].execute().waitForProcessOutput()
    }
}
