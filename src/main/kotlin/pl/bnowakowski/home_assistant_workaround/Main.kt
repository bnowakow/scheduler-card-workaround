package pl.bnowakowski.home_assistant_workaround

import kotlin.system.exitProcess


fun main() {

    val homeAssistant = HomeAssistant()

    homeAssistant.login()
    homeAssistant.iterateThroughSchedulesAndToggleThem()
    homeAssistant.iterateThroughSwitchesInAlwaysOnGroupAndToggleThem()
    homeAssistant.finish()

    exitProcess(0)
}

