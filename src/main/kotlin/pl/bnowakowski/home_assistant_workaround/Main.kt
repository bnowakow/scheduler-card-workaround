package pl.bnowakowski.home_assistant_workaround

import mu.KotlinLogging


fun main() {
    val logger = KotlinLogging.logger {}

    val homeAssistant: HomeAssistant = HomeAssistant()

    homeAssistant.login()
}
