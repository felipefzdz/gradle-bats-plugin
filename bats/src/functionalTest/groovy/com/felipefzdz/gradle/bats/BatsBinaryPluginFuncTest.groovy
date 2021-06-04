package com.felipefzdz.gradle.bats

import spock.lang.IgnoreIf

@IgnoreIf({ env['BATS_PATH'] == null })
class BatsBinaryPluginFuncTest extends BaseBatsPluginFuncTest {
    boolean useDocker = false
    String batsBinary = System.getenv('BATS_PATH')
}
