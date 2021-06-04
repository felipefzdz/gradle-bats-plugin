package com.felipefzdz.gradle.bats

abstract class BaseBatsPluginFuncTest extends BaseInfraTest {

    def "fail the build when some test fails"() {
        given:
        buildFile << """
bats {
    sources = files("${resources.absolutePath}/failure_tests")
    useDocker = $useDocker
    batsBinary = "$batsBinary"
}
"""

        when:
        def result = runner().buildAndFail()

        then:
        result.getOutput().contains("not ok 1 can run our script")
    }

    def "pass the build when every test passes"() {
        given:
        buildFile << """
bats {
    sources = files("${resources.absolutePath}/successful_tests")
    useDocker = $useDocker
    batsBinary = "$batsBinary"
}
"""

        expect:
        runner().build()
    }
}
