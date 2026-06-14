package com.autotests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Test Automation Playground")
@Feature("Dynamic ID")
@DisplayName("Dynamic ID Tests")
public class DynamicIdTest extends BaseUiTest {

    private static final String PAGE_URL = BASE_URL + "/dynamicid";

    @Test
    @Story("Dynamic ID")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Dynamic ID — клик по кнопке с динамическим ID")
    @Description("Проверяет возможность нажатия на кнопку, у которой ID генерируется динамически " +
            "при каждой загрузке страницы. Нельзя использовать ID для поиска элемента.")
    void testClickButtonWithDynamicId() {
        log.info("Opening Dynamic ID page: {}", PAGE_URL);
        driver.get(PAGE_URL);

        // Find button by its text content, not by ID (since ID is dynamic)
        WebElement button = driver.findElement(
                By.xpath("//button[contains(@class, 'btn-primary')]"));
        assertNotNull(button, "Button should be found by class");
        assertTrue(button.isDisplayed(), "Button should be visible");

        String buttonText = button.getText();
        log.info("Found button with text: '{}'", buttonText);
        assertEquals("Button with Dynamic ID", buttonText);

        // Click the button
        button.click();
        log.info("Successfully clicked button with dynamic ID");

        // Verify button is still present after click (it should remain on page)
        WebElement buttonAfterClick = driver.findElement(
                By.xpath("//button[contains(@class, 'btn-primary')]"));
        assertTrue(buttonAfterClick.isDisplayed(),
                "Button should still be visible after click");

        log.info("Dynamic ID test completed successfully");
    }
}
