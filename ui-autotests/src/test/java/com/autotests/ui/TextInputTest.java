package com.autotests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Test Automation Playground")
@Feature("Text Input")
@DisplayName("Text Input Tests")
public class TextInputTest extends BaseUiTest {

    private static final String PAGE_URL = BASE_URL + "/textinput";

    @Test
    @Story("Text Input")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Text Input — ввод текста меняет название кнопки")
    @Description("Проверяет, что после ввода текста в поле и нажатия кнопки, " +
            "название кнопки меняется на введённый текст.")
    void testTextInputChangesButtonName() {
        log.info("Opening Text Input page: {}", PAGE_URL);
        driver.get(PAGE_URL);

        String newButtonName = "MyCustomButton";

        // Find the input field
        WebElement inputField = driver.findElement(By.id("newButtonName"));
        assertNotNull(inputField, "Input field should be present");

        // Find the button
        WebElement button = driver.findElement(By.id("updatingButton"));
        String originalName = button.getText();
        log.info("Original button name: '{}'", originalName);
        assertNotEquals(newButtonName, originalName,
                "New name should differ from original");

        // Enter new button name
        inputField.clear();
        inputField.sendKeys(newButtonName);
        log.info("Entered new button name: '{}'", newButtonName);

        // Click the button to trigger name change
        button.click();
        log.info("Clicked the button");

        // Verify button name has changed
        button = driver.findElement(By.id("updatingButton"));
        String updatedName = button.getText();
        assertEquals(newButtonName, updatedName,
                "Button name should change to the entered text");

        log.info("Button name changed to: '{}'", updatedName);
        log.info("Text Input test completed successfully");
    }
}
