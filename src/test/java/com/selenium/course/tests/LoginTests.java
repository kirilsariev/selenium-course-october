package com.selenium.course.tests;

import java.io.IOException;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.exceptions.CsvException;
import com.selenium.course.base.TestUtil;
import com.selenium.course.utils.CsvReader;

public class LoginTests extends TestUtil {

    @DataProvider(name="login-data")
    public static Object[][] dataProviderHardcodedData(){
        return new Object[][]{
                {"user1", "pass1", "Epic sadface: Username and password do not match any user in this service"},
                {"user2", "pass2", "Epic sadface: Username and password do not match any user in this service"},
                {"user3", "pass3", "Username and password do not match any user in this service"}
        };
    }

    @DataProvider(name="login-data-file")
    public static Object[][] dataProviderFromCsvFile() throws IOException, CsvException {
        return CsvReader.readCsvFile("src/test/resources/login-data.csv");
    }

    @Test(dataProvider = "login-data")
    @Description("Login multiple times with different users.")
    @Severity(SeverityLevel.CRITICAL)
    public void executeLogin(String user, String pass, String errMsg) {
        loginWith(user, pass);
        verifyLoginErrMsg(errMsg);
        pageRefresh();
    }

    @Step("Try to login with credentials: {user} / {pass}.")
    public void loginWith(String user, String pass) {
        WebElement username = driver.findElement(By.id("user-name"));
        username.sendKeys(user);

        WebElement password = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        password.sendKeys(pass);

        WebElement loginButton = driver.findElement(By.className("btn_action"));
//        loginButton.click();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", loginButton);
        js.executeScript("arguments[0].click();", loginButton);
    }

    @Step("Verify that login error message is: {errMsg}.")
    public void verifyLoginErrMsg(String errMsg) {
        saveScreenshot(driver);
        String actualErrMsg = driver.findElement(By.id("login_button_container")).getText();
        Assert.assertEquals(actualErrMsg, errMsg);
    }

    @Step("Refreshing the page")
    public void pageRefresh(){
        driver.navigate().refresh();
    }
}
