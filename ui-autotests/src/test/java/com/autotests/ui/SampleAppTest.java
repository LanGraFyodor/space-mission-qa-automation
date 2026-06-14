package com.autotests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Test Automation Playground")
@Feature("Sample App")
@DisplayName("Sample App Tests")
public class SampleAppTest extends BaseUiTest {

    private static final String PAGE_URL = BASE_URL + "/sampleapp";

    @Test
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Sample App — успешный логин")
    @Description("Проверяет успешный логин с корректными учётными данными (любой username, пароль 'pwd'). " +
            "После логина статус должен измениться на 'Welcome, <username>!'")
    void testSuccessfulLogin() {
        log.info("Opening Sample App page: {}", PAGE_URL);
        driver.get(PAGE_URL);

        // Wait for page to be fully loaded
        wait.until(webDriver ->
                ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));

        log.info("Page loaded. URL is: {}", driver.getCurrentUrl());
        
        String username = "TestUser";

        try {
            // Wait for and verify initial state
            WebElement loginStatus = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("loginstatus")));
            assertTrue(loginStatus.getText().contains("User logged out"),
                    "Initial status should be 'User logged out'");
            log.info("Initial status: {}", loginStatus.getText().trim());

            // Wait for and fill in credentials
            WebElement usernameInput = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='UserName' or @id='username']")));
            usernameInput.clear();
            usernameInput.sendKeys(username);
            log.info("Entered username: {}", username);

            WebElement passwordInput = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='Password' or @id='password']")));
            passwordInput.clear();
            passwordInput.sendKeys("pwd");
            log.info("Entered password");
            
            // Click login button
            WebElement loginButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("login")));
            loginButton.click();
            log.info("Clicked Login button");

            // Verify login success
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.id("loginstatus"), "Welcome"));

            loginStatus = driver.findElement(By.id("loginstatus"));
            String expectedText = "Welcome, " + username + "!";
            assertEquals(expectedText, loginStatus.getText().trim(),
                    "Status should show welcome message for user");

            log.info("Login successful. Status: {}", loginStatus.getText().trim());
        } catch (Exception e) {
            log.error("Failed to find elements. Page source: \n{}", driver.getPageSource());
            throw e;
        }
    }
}
