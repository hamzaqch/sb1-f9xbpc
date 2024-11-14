package com.mapbox.automation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = ConfigReader.getProperty("browser").toLowerCase();
            boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
            
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) chromeOptions.addArguments("--headless");
                    driver.set(new ChromeDriver(chromeOptions));
                    break;
                    
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) firefoxOptions.addArguments("--headless");
                    driver.set(new FirefoxDriver(firefoxOptions));
                    break;
                    
                default:
                    throw new RuntimeException("Unsupported browser: " + browser);
            }

            WebDriver webDriver = driver.get();
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.getProperty("implicit.wait"))));
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.getProperty("page.load.timeout"))));
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}