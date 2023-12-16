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
        // laptop screen
//        driver.manage().window().position = Point(800, 0)
        // desktop screen
        driver.manage().window().position = Point(1490, 0)


        driver
        js = driver as JavascriptExecutor
//        driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, "-"))

        // Chrome
        //driver = ChromeDriver()

        // Safari
        //driver = SafariDriver()
    }

    public fun login() {

        val homeAssistantProperties = HomeAssistantProperties()

        driver["https://home-assistant.localdomain.bnowakowski.pl:8123/"]
        // for some reason home assistant refreshes itself, so we wait to counter that
        Thread.sleep(10000)
        driver.findElement(By.name("username")).sendKeys(homeAssistantProperties.getProperty("home-assistant.username"))
        driver.findElement(By.name("password")).sendKeys(homeAssistantProperties.getProperty("home-assistant.password"))
        Thread.sleep(500)
//        driver.findElement(By.id("button")).click()
        // workaround for above
        for (i in 1..3) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.TAB)
            Thread.sleep(100)
        }
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.RETURN)
        Thread.sleep(6000)
    }

}