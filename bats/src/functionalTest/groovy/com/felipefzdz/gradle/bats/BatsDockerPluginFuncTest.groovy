package com.felipefzdz.gradle.bats

import org.gradle.testkit.runner.GradleRunner

class BatsDockerPluginFuncTest extends BaseBatsPluginFuncTest {
    boolean useDocker = true
    String batsBinary = "bats"

    def "bats task can be loaded from the configuration cache"() {
        given:
        buildFile << """
bats {
    sources = files("${resources.absolutePath}/successful_tests")
    useDocker = $useDocker
    batsBinary = "$batsBinary" 
}
"""

        when:
        runnerWithConfigurationCache().build()

        and:
        def result = runnerWithConfigurationCache().build()

        then:
        result.output.contains('Reusing configuration cache.')
    }

    def "PATH and HOME are not stripped from the env to ensure Docker for Mac compatibility"() {
        given:
        buildFile << """
bats {
    sources = files("${resources.absolutePath}/successful_tests")
    useDocker = $useDocker
    batsBinary = "$batsBinary" 
}
"""

        when:
        def result = runnerWithDebugLogging().build().output

        then:
        result.contains("Environment keys after preparing it for isolated Bats execution: [PATH, HOME]")
    }

    private GradleRunner runnerWithConfigurationCache() {
        runner(false, false, true)
    }
}