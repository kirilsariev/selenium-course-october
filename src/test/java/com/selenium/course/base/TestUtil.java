package com.selenium.course.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.selenium.course.driver.DriverFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUtil {

    private String url;
    private int implicitWait;
    public WebDriver driver;

    @BeforeSuite(description = "Reading configuration properties.")
    public void readConfigProperties() {
        try(FileInputStream configFile = new FileInputStream("src/test/resources/config.properties")){
            Properties config = new Properties();
            config.load(configFile);
            url = config.getProperty("urlAddress");
            implicitWait = Integer.parseInt(config.getProperty("implicitWait"));
            // browser to be taken from property file
            log.info("Url is: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeTest(description = "Setting up browser and loading URL.")
    public void initTest() {
        setupBrowserDriver();
        loadUrl();
    }

    @AfterTest(description = "Closing browser.")
    public void tearDownDriver() {
        driver.quit();
    }

    private void setupBrowserDriver(){
        driver = DriverFactory.getFirefoxDriver(implicitWait);
        // chrome implementation?
    }

    private void loadUrl(){
        driver.get(url);
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
