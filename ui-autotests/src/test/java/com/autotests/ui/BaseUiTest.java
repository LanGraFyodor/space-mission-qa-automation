package com.autotests.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.time.Duration;

/**
 * Base class for all UI tests.
 * Manages WebDriver lifecycle, screenshots, and common configuration.
 */
public abstract class BaseUiTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseUiTest.class);
    protected static final String BASE_URL = "http://uitestingplayground.com";

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeAll
    static void setupDriver() {
        WebDriverManager.chromedriver().setup();
        log.info("ChromeDriver setup complete");
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();

        // Run headless if system property is set
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        log.info("Browser started for test");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            try {
                // Attach screenshot to Allure
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot", "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                log.warn("Could not take screenshot: {}", e.getMessage());
            }
            driver.quit();
            log.info("Browser closed");
        }
    }
}
