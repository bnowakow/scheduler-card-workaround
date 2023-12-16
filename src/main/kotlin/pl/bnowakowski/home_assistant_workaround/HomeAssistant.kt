package pl.bnowakowski.home_assistant_workaround

import mu.KotlinLogging
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.safari.SafariDriver
import kotlin.NoSuchElementException

class HomeAssistant {

    private var driver: WebDriver
    private val js: JavascriptExecutor
    private val homeAssistantProperties = HomeAssistantProperties()


    init {
//         Chrome
//        driver = ChromeDriver()

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

        driver[homeAssistantProperties.getProperty("home-assistant.url")+"/lovelace"]
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

        for (i in 1..4) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
        }
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.RETURN)
        Thread.sleep(2000)
        for (i in 1..13) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
        }

        val numberOfSchedules: Int = homeAssistantProperties.getProperty("home-assistant.number-of-schedules").toInt()


        for (i in 1..numberOfSchedules) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
            Thread.sleep(5000)
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.SPACE)
            Thread.sleep(100)
        }

        Thread.sleep(500)
    }

    fun finish() {
        driver.close()
    }

}