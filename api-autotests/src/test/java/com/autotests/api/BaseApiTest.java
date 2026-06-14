package com.autotests.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Base class for all API tests.
 * Handles configuration, authentication, and common setup.
 */
public abstract class BaseApiTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseApiTest.class);

    protected static String baseUrl;
    protected static String token;

    @BeforeAll
    static void globalSetup() {
        Properties props = new Properties();
        try (InputStream is = BaseApiTest.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            log.error("Failed to load application.properties", e);
        }

        baseUrl = props.getProperty("base.url", "http://localhost:8080");
        String username = props.getProperty("test.username", "admin");
        String password = props.getProperty("test.password", "password");

        RestAssured.baseURI = baseUrl;
        RestAssured.filters(new AllureRestAssured());

        log.info("API tests configured with base URL: {}", baseUrl);

        // Authenticate and get JWT token
        token = authenticate(username, password);
        log.info("Successfully authenticated as: {}", username);
    }

    private static String authenticate(String username, String password) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getString("token");
    }

    /**
     * Creates an authorized request specification with JWT token.
     */
    protected static io.restassured.specification.RequestSpecification authorized() {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }
}
