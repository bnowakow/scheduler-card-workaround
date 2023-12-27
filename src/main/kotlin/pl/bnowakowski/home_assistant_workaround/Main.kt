package pl.bnowakowski.home_assistant_workaround

import kotlin.system.exitProcess


fun main() {

    val homeAssistant = HomeAssistant()

    // TODO read args and execute 1 or 2 or 1 and 2

    // 1
    homeAssistant.login()
    homeAssistant.iterateThroughSchedulesAndToggleThem()

    //2
    homeAssistant.iterateThroughSwitchesInAlwaysOnGroupAndToggleThem()

    // run always
    homeAssistant.finish()

    exitProcess(0)
}

