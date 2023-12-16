package pl.bnowakowski.home_assistant_workaround

import mu.KotlinLogging
import org.openqa.selenium.*
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import kotlin.NoSuchElementException
class HomeAssistant {

    private var driver: WebDriver
    private val js: JavascriptExecutor


    init {
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
        driver.manage().window().position = Point(800, 0)

        driver["https://home-assistant.localdomain.bnowakowski.pl:8123/"]
        js = driver as JavascriptExecutor
//        driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, "-"))

        // Chrome
        //driver = ChromeDriver()

        // Safari
        //driver = SafariDriver()
    }

    public fun login() {

//        val facebookProperties = FacebookProperties()

        driver["https://home-assistant.localdomain.bnowakowski.pl:8123/"]
    }

}