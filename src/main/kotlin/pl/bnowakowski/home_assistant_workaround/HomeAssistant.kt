package pl.bnowakowski.home_assistant_workaround

import mu.KotlinLogging
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile


class HomeAssistant {

    private var driver: WebDriver
    private val js: JavascriptExecutor
    private val homeAssistantProperties = HomeAssistantProperties()

    private val logger = KotlinLogging.logger {}


    init {
//         Chrome
          // https://www.browserstack.com/guide/selenium-headless-browser-testing
//        val options = ChromeOptions()
//        options.addArguments("--headless")
//        //        driver = ChromeDriver(options)

        // Safari
//        driver = SafariDriver()

        // Firefox
        // https://www.browserstack.com/docs/automate/selenium/firefox-profile
        val firefoxProfile = FirefoxProfile()
//        firefoxProfile.setPreference("layout.css.devPixelsPerPx", "2.0")

        // https://stackoverflow.com/questions/15397483/how-do-i-set-browser-width-and-height-in-selenium-webdriver
        val firefoxOptions = FirefoxOptions()
        firefoxOptions.addArguments("--width=1000")
        firefoxOptions.addArguments("--height=3440")
        // https://github.com/mdn/headless-examples/blob/master/headlessfirefox-gradle/src/main/java/com/mozilla/example/HeadlessFirefoxSeleniumExample.java
        firefoxOptions.addArguments("--headless")
        firefoxOptions.profile = firefoxProfile

        driver = FirefoxDriver(firefoxOptions)
        // laptop screen
//        driver.manage().window().position = Point(800, 0)
        // desktop screen
        driver.manage().window().position = Point(1490, 0)


        driver
        js = driver as JavascriptExecutor
//        driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, "-"))
    }

    fun login() {

        val loggingUrl = homeAssistantProperties.getProperty("home-assistant.url")+"/lovelace"
        logger.debug("openning url=$loggingUrl")
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
//    private fun countOccurrences(str: String, searchStr: String): Int {
//        var count = 0
//        var startIndex = 0
//
//        while (startIndex < str.length) {
//            val index = str.indexOf(searchStr, startIndex)
//            if (index >= 0) {
//                count++
//                startIndex = index + searchStr.length
//            } else {
//                break
//            }
//        }
//
//        return count
//    }

    fun iterateThroughSchedulesAndSaveOnEach() {

        // there's a problem with pagesource so instead going with unreliable tabs for this time
//        driver[homeAssistantProperties.getProperty("home-assistant.url")+"/dashboard-scheduler"]
//        Thread.sleep(2000)
        //        println(countOccurrences(driver.pageSource, "hours"))
        // TODO check if when scheduler-card will display toogle for all schedules is it enough to toggle that one instead going one by one

        for (i in 1..4) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
        }
        logger.debug("trying to open Schedules dashboard")
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.RETURN)
        Thread.sleep(2000)
        for (i in 1..13) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
        }

        val numberOfSchedules: Int = homeAssistantProperties.getProperty("home-assistant.number-of-schedules").toInt()
        logger.debug("got numberOfSchedules=$numberOfSchedules")

        for (i in 1..numberOfSchedules) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
            logger.debug("trying to turn off i=$i schedule")
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
            Thread.sleep(5000)
            logger.debug("trying to turn on i=$i schedule")
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
            Thread.sleep(100)
        }
        logger.debug("finished all schedules")

        Thread.sleep(500)
    }

    fun finish() {
        logger.debug("closing firefox")
        driver.close()
    }

}