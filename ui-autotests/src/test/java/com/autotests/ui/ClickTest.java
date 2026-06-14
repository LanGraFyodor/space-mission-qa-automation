package com.autotests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Test Automation Playground")
@Feature("Click")
@DisplayName("Click Tests")
public class ClickTest extends BaseUiTest {

    private static final String PAGE_URL = BASE_URL + "/click";

    @Test
    @Story("Click")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Click — физический клик меняет цвет кнопки на зелёный")
    @Description("Проверяет, что после физического клика по кнопке она меняет класс с 'btn-primary' " +
            "(синий) на 'btn-success' (зелёный). Страница специально игнорирует DOM-клики.")
    void testPhysicalClickChangesButtonColor() {
        log.info("Opening Click page: {}", PAGE_URL);
        driver.get(PAGE_URL);

        WebElement button = driver.findElement(By.id("badButton"));
        assertNotNull(button, "Button should be present");
        log.info("Found button: '{}'", button.getText());

        // Verify initial state - button is blue (btn-primary)
        String initialClass = button.getAttribute("class");
        assertTrue(initialClass.contains("btn-primary"),
                "Button should initially have 'btn-primary' class");
        log.info("Initial button class: {}", initialClass);

        // Perform click using Actions (physical click) to handle the DOM click ignore
        org.openqa.selenium.interactions.Actions actions =
                new org.openqa.selenium.interactions.Actions(driver);
        actions.moveToElement(button).click().perform();
        log.info("Performed physical click on button");

        // Wait for button to change color
        wait.until(ExpectedConditions.attributeContains(
                By.id("badButton"), "class", "btn-success"));

        // Verify button changed to green
        String newClass = button.getAttribute("class");
        assertTrue(newClass.contains("btn-success"),
                "Button should have 'btn-success' class after click");
        log.info("Button class after click: {}", newClass);

        log.info("Click test completed successfully");
    }
}
