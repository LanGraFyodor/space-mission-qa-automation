package com.autotests.api;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Space Mission Management API")
@Feature("Authentication")
@DisplayName("Auth API Tests")
public class AuthApiTest extends BaseApiTest {

    @Test
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /api/auth/login — успешная авторизация")
    @Description("Проверяет успешную авторизацию с корректными учётными данными")
    void testSuccessfulLogin() {
        log.info("Testing successful login");

        given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"testuser\", \"password\": \"password\"}")
        .when()
                .post("/api/auth/login")
        .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("username", equalTo("testuser"));
    }

    @Test
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /api/auth/login — неверный пароль (401)")
    @Description("Проверяет отказ в авторизации при неверном пароле")
    void testLoginInvalidPassword() {
        log.info("Testing login with invalid password");

        given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"testuser\", \"password\": \"wrong\"}")
        .when()
                .post("/api/auth/login")
        .then()
                .statusCode(401)
                .body("error", notNullValue());
    }

    @Test
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /api/auth/login — пустой username (400)")
    @Description("Проверяет валидацию пустого имени пользователя")
    void testLoginEmptyUsername() {
        log.info("Testing login with empty username");

        given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"\", \"password\": \"password\"}")
        .when()
                .post("/api/auth/login")
        .then()
                .statusCode(400);
    }
}
