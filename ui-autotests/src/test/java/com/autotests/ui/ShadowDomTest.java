package com.autotests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Test Automation Playground")
@Feature("Shadow DOM")
@DisplayName("Shadow DOM Tests")
public class ShadowDomTest extends BaseUiTest {

    private static final String PAGE_URL = BASE_URL + "/shadowdom";

    @Test
    @Story("Shadow DOM")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Shadow DOM — доступ к элементам внутри Shadow DOM")
    @Description("Проверяет возможность доступа к элементам, скрытым внутри Shadow DOM. " +
            "Аналог работы с фреймами — вложенные элементы, требующие особого подхода.")
    void testShadowDomElements() {
        log.info("Opening Shadow DOM page: {}", PAGE_URL);
        driver.get(PAGE_URL);

        // Find the shadow host element
        WebElement shadowHost = driver.findElement(By.cssSelector("guid-generator"));
        log.info("Found shadow host element: guid-generator");

        // Access shadow root
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        assertNotNull(shadowRoot, "Shadow root should be accessible");
        log.info("Successfully accessed shadow root");

        // Find the 'Generate' button inside shadow DOM
        WebElement generateButton = shadowRoot.findElement(By.cssSelector("#buttonGenerate"));
        assertNotNull(generateButton, "Generate button should exist in shadow DOM");
        assertTrue(generateButton.isDisplayed(), "Generate button should be visible");
        log.info("Found Generate button inside shadow DOM");

        // Click the generate button
        generateButton.click();
        log.info("Clicked Generate button");

        // Find the 'Copy' button inside shadow DOM
        WebElement copyButton = shadowRoot.findElement(By.cssSelector("#buttonCopy"));
        assertNotNull(copyButton, "Copy button should exist in shadow DOM");
        assertTrue(copyButton.isDisplayed(), "Copy button should be visible");
        log.info("Found Copy button inside shadow DOM");

        // Find the generated GUID input field
        WebElement guidInput = shadowRoot.findElement(By.cssSelector("#editField"));
        assertNotNull(guidInput, "GUID input field should exist");
        String generatedGuid = guidInput.getAttribute("value");
        assertNotNull(generatedGuid, "Generated GUID should not be null");
        assertFalse(generatedGuid.isEmpty(), "Generated GUID should not be empty");
        log.info("Generated GUID: {}", generatedGuid);

        log.info("Shadow DOM test completed successfully");
    }
}
