package pl.bnowakowski.home_assistant_workaround

import kotlin.system.exitProcess


fun main() {

    val homeAssistant = HomeAssistant()

    homeAssistant.iterateThroughSchedulesAndToggleThem()
    homeAssistant.iterateThroughSwitchesInAlwaysOnGroupAndToggleThem()

    // run always
    homeAssistant.finish()

    exitProcess(0)
}

