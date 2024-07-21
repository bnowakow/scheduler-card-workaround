package pl.bnowakowski.home_assistant_workaround

import mu.KotlinLogging
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebElement


class HomeAssistant {

    private var driver: WebDriver
    private val js: JavascriptExecutor
    private val homeAssistantProperties = HomeAssistantProperties()
    // in iterateThroughSwitchesInAlwaysOnGroupAndToggleThem html single switch could be added twice with different endpoints (left and right button), using set so devices wouldn't be duplicated
    private var switchDeviceUrls: MutableSet<String> = LinkedHashSet()

    private val logger = KotlinLogging.logger {}


    init {
//         Chrome
          // https://www.browserstack.com/guide/selenium-headless-browser-testing
        val options = ChromeOptions()
        if (homeAssistantProperties.getProperty("browser.headless").toBoolean()) {
            logger.debug("running browser in headless mode")
            options.addArguments("--no-sandbox")
            options.addArguments("--headless=new")
            options.addArguments("--disable-gpu")
            options.addArguments("--disable-dev-shm-usage")
        }
        driver = ChromeDriver(options)

        // Safari
//        driver = SafariDriver()

//        // Firefox
//        // https://www.browserstack.com/docs/automate/selenium/firefox-profile
//        val firefoxProfile = FirefoxProfile()
//
//        // https://stackoverflow.com/questions/15397483/how-do-i-set-browser-width-and-height-in-selenium-webdriver
//        val firefoxOptions = FirefoxOptions()
//
//        // https://github.com/mdn/headless-examples/blob/master/headlessfirefox-gradle/src/main/java/com/mozilla/example/HeadlessFirefoxSeleniumExample.java
//        if (homeAssistantProperties.getProperty("browser.headless").toBoolean()) {
//            logger.debug("running browser in headless mode")
//            firefoxOptions.addArguments("--headless")
//        } else {
//            logger.debug("running browser in non-headless mode")
//            // https://stackoverflow.com/questions/15397483/how-do-i-set-browser-width-and-height-in-selenium-webdriver
//            firefoxOptions.addArguments("--width=1000")
//            firefoxOptions.addArguments("--height=3440")
//        }
//        firefoxOptions.profile = firefoxProfile
//
//        driver = FirefoxDriver(firefoxOptions)
        if (!homeAssistantProperties.getProperty("browser.headless").toBoolean()) {
            // laptop screen
//        driver.manage().window().position = Point(800, 0)
            // desktop screen
            driver.manage().window().position = Point(1490, 0)
        }


        driver
        js = driver as JavascriptExecutor
//        driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, "-"))
    }

    private fun login() {

        val loggingUrl = homeAssistantProperties.getProperty("home-assistant.url")+"/lovelace"
        logger.info("openning url=$loggingUrl")
        driver[loggingUrl]
        // for some reason home assistant refreshes itself, so we wait to counter that
        Thread.sleep(10000)
        driver.findElement(By.name("username")).sendKeys(homeAssistantProperties.getProperty("home-assistant.username"))
        driver.findElement(By.name("password")).sendKeys(homeAssistantProperties.getProperty("home-assistant.password"))
        Thread.sleep(500)
        /// driver.findElement(By.id("button")).click()
        // workaround for above
        for (i in 1..2) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
        }
        // selecting keep me logged in since without it reload of this tab will log us out
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
        Thread.sleep(100)
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
        Thread.sleep(100)
        logger.debug("trying to press sign in button")
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.RETURN)
        Thread.sleep(2000)
    }

//    // https://kotlinandroid.org/kotlin/kotlin-count-occurrences-of-a-substring-in-a-string/
    private fun countOccurrences(str: String, searchStr: String): Int {
        var count = 0
        var startIndex = 0

        while (startIndex < str.length) {
            val index = str.indexOf(searchStr, startIndex)
            if (index >= 0) {
                count++
                startIndex = index + searchStr.length
            } else {
                break
            }
        }

        return count
    }

    fun iterateThroughSchedulesAndToggleThem() {

        if (homeAssistantProperties.getProperty("home-assistant.toggle-schedules-enabled") == "true") {

            login()

            // there's a problem with pagesource so instead going with unreliable tabs for this time
//        driver[homeAssistantProperties.getProperty("home-assistant.url")+"/dashboard-scheduler"]
//        Thread.sleep(2000)
            //        println(countOccurrences(driver.pageSource, "hours"))
            // TODO check if when scheduler-card will display toogle for all schedules is it enough to toggle that one instead going one by one

//        driver.navigate().refresh()
//        Thread.sleep(10000)
//        driver.findElement(By.cssSelector("body")).sendKeys(Keys.F5)
//        Thread.sleep(10000)


            for (i in 1..5) {
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
                Thread.sleep(100)
                val elementText = driver.switchTo().activeElement().text
                logger.debug("tab i=$i element_txt=$elementText")
                logger.debug("\thtml=" + driver.switchTo().activeElement().getAttribute("innerHTML"))
            }
            logger.debug("\thtml=" + driver.switchTo().activeElement().getAttribute("innerHTML"))
            logger.debug("trying to open Schedules dashboard")
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.RETURN)
            Thread.sleep(2000)
            for (i in 1..12) {
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
                Thread.sleep(100)
                val elementText = driver.switchTo().activeElement().text
                logger.debug("tab i=$i element_txt=$elementText")
                logger.debug("\thtml=" + driver.switchTo().activeElement().getAttribute("innerHTML"))
            }

            // TODO maybe add stupid screenshot check that we're
            //      1. on schedules dashboard
            //      2. that focus is before first schedule switch (if focused element is not visible then check just before first toggle so switch will be visibly focused


            val numberOfSchedules: Int =
                homeAssistantProperties.getProperty("home-assistant.number-of-schedules").toInt()
            logger.debug("got numberOfSchedules=$numberOfSchedules")

            for (i in 1..numberOfSchedules) {
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
                Thread.sleep(100)
                val elementText = driver.switchTo().activeElement().text
                logger.debug("tab i=$i element_txt=$elementText")
                logger.debug("\thtml=" + driver.switchTo().activeElement().getAttribute("innerHTML"))
                logger.debug("trying to turn off i=$i schedule")
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
                Thread.sleep(5000)
                val elementText2 = driver.switchTo().activeElement().text
                logger.debug("tab i=$i element_txt=$elementText2")
                logger.debug("\thtml=" + driver.switchTo().activeElement().getAttribute("innerHTML"))
                logger.debug("trying to turn on i=$i schedule")
                driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
                Thread.sleep(100)
            }
            logger.debug("finished all schedules")

            Thread.sleep(500)
        }
    }

    private fun tabUntilAttributeEquals(attributeName :String, expectedValue: String, matchInsteadOfExact: Boolean = false) {
        var i = 0
        while (true) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
            val elementText = driver.switchTo().activeElement().text
            logger.debug("tab i=$i element_txt=$elementText")
            try {
                val value = driver.switchTo().activeElement().getAttribute(attributeName)
                if (!matchInsteadOfExact) {
                    // exact
                    if (value.equals(expectedValue)) {
                        break
                    }
                } else {
                    // match
                    if (value.contains(expectedValue)) {
                        break
                    }
                }
            } catch( e: NullPointerException) {
            }
            i++
        }
    }

    private fun tabUntilNextSwitchAndCollectItsUrl() {
        val attributeName: String = "href"
        tabUntilAttributeEquals(attributeName, "/device/", matchInsteadOfExact = true)
        switchDeviceUrls.add(driver.switchTo().activeElement().getAttribute(attributeName))
    }

    fun iterateThroughSwitchesInAlwaysOnGroupAndToggleThem() {

        if (homeAssistantProperties.getProperty("zigbee2mqtt.iterate-through-switches-in-always-on-group-and-toggle-them-enabled") == "true") {

            val zigbe2mqttUrl = homeAssistantProperties.getProperty("zigbee2mqtt.url") + "/#/group/4"
            logger.info("openning url=$zigbe2mqttUrl")
            driver[zigbe2mqttUrl]

            Thread.sleep(2000)

            tabUntilNextSwitchAndCollectItsUrl()
            tabUntilAttributeEquals("class", "btn btn-danger btn-sm float-right")

            val numberOfSwitches: Int = countOccurrences(driver.pageSource, "LQI")
            logger.debug("got numberOfSwitches=$numberOfSwitches")

            for (i in 1..numberOfSwitches) {


                // TODO read enpoint number and toggle only appriopriate switch
                for (j in 1..2) {
                    tabUntilAttributeEquals("class", "form-check-input")

                    val elementClass = driver.switchTo().activeElement().getAttribute("class")
                    logger.debug("tab i=$i elementClass=$elementClass")

                    var isSwitchEnabled: Boolean = false
                    isSwitchEnabled = try {
                        driver.switchTo().activeElement().getAttribute("checked").equals("true")
                    } catch (e: NullPointerException) {
                        false
                    }

                    val numberOfToggles = if (isSwitchEnabled) {
                        // TODO do a force option and set it to 2 (when switch state is stucked)

                        if (homeAssistantProperties.getProperty("zigbee2mqtt.force-toggle-when-switch-is-already-on") == "true") {
                            2
                        } else {
                            0
                        }
                    } else {
                        3
                    }
                    logger.debug("switch i=$i j=$j isEnabled=$isSwitchEnabled numberOfToggles=$numberOfToggles")

                    for (k in 1..numberOfToggles) {
                        driver.switchTo().activeElement().sendKeys(Keys.SPACE)
                        Thread.sleep(1000)
                    }
                }
                tabUntilNextSwitchAndCollectItsUrl()
            }
            logger.debug("finished all switches")

            Thread.sleep(2000)

            iterateThroughSwitchesInAlwaysOnGroupAndSetDecoupleMode()
        }
    }

    private fun tabUntilOperationModeIsFocused() {
        tabUntilAttributeEquals("class", "btn btn-outline-secondary", matchInsteadOfExact = true)
    }

    private fun tabUntilOperationModeIsFocusedAndGoBackToRefreshButton() {
        tabUntilOperationModeIsFocused()
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.SHIFT, Keys.TAB))
    }

    private fun iterateThroughSwitchesInAlwaysOnGroupAndSetDecoupleMode() {

        for (switchDeviceAboutUrl in switchDeviceUrls) {
            val switchDeviceExposesUrl: String = "$switchDeviceAboutUrl/exposes"
            logger.info("openning url=$switchDeviceExposesUrl")
            driver[switchDeviceExposesUrl]

            Thread.sleep(3000)

            tabUntilAttributeEquals("aria-label", "Select a device")

            // TODO in iterateThroughSwitchesInAlwaysOnGroupAndToggleThem html single switch could be added twice with different endpoints (left and right button), if we turn on given endpoint then in here we set decoupled to all endpoints instead of selected one, check it
            for (i in 1..2) {
                tabUntilOperationModeIsFocusedAndGoBackToRefreshButton()

                // check if previous sibling has label for Decoupled mode
                var previousSibling: WebElement = RemoteWebElement()
                try {
                    previousSibling =
                        driver.switchTo().activeElement().findElement(By.xpath("preceding-sibling::strong[1]"))
                } catch(e: NoSuchElementException) {
                }
                if (previousSibling.getAttribute("title").contains("Decoupled mode for")) {
                    // click refresh
                    logger.info("clicking refresh button for operation mode")
                    driver.switchTo().activeElement().sendKeys(Keys.SPACE)
                    Thread.sleep(4000)
                    // decouple operation mode is second after control_relay
                    for (j in 1..2) {
                        tabUntilOperationModeIsFocused()
                    }

                    if (driver.switchTo().activeElement().text.equals("decoupled")) {
                        logger.info("clicking decoupled")
                        // TODO for some reason in Arc html returns coupled mode for same device that in this Firefox returns decoupled :/
//                        driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
                        driver.switchTo().activeElement().sendKeys(Keys.SPACE)
                    } else {
                        logger.error("active element wasn't decoupled operation mode")
                    }
                } else {
                    logger.error("active element wasn't refresh button for operation mode")
                }

                Thread.sleep(2000)
            }

            println("debug")
        }
    }

    fun finish() {
        logger.debug("closing firefox")
        driver.close()
    }

}