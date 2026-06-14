package com.autotests.api;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

@Epic("Space Mission Management API")
@Feature("Satellites")
@DisplayName("Satellite API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SatelliteApiTest extends BaseApiTest {

    private static int createdSatelliteId;

    @Test
    @Order(1)
    @Story("Get Satellites")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /api/satellites — получение списка спутников")
    @Description("Проверяет получение списка всех спутников")
    void testGetAllSatellites() {
        log.info("Testing GET all satellites");

        authorized()
        .when()
                .get("/api/satellites")
        .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    @Order(2)
    @Story("Create Satellite")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /api/satellites — создание спутника")
    @Description("Проверяет создание нового спутника")
    void testCreateSatellite() {
        log.info("Testing POST create satellite");

        String body = """
                {
                    "name": "TEST-SAT-001",
                    "type": "OBSERVATION",
                    "orbitType": "LEO",
                    "status": "ACTIVE",
                    "groupId": null
                }
                """;

        createdSatelliteId = authorized()
                .body(body)
        .when()
                .post("/api/satellites")
        .then()
                .statusCode(201)
                .body("name", equalTo("TEST-SAT-001"))
                .body("type", equalTo("OBSERVATION"))
                .body("orbitType", equalTo("LEO"))
                .body("status", equalTo("ACTIVE"))
                .body("id", notNullValue())
                .extract()
                .jsonPath().getInt("id");

        log.info("Created satellite with id: {}", createdSatelliteId);
    }

    @Test
    @Order(3)
    @Story("Get Satellite")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /api/satellites/{id} — получение спутника по ID")
    @Description("Проверяет получение созданного спутника по его ID")
    void testGetSatelliteById() {
        log.info("Testing GET satellite by id: {}", createdSatelliteId);

        authorized()
        .when()
                .get("/api/satellites/" + createdSatelliteId)
        .then()
                .statusCode(200)
                .body("id", equalTo(createdSatelliteId))
                .body("name", equalTo("TEST-SAT-001"));
    }

    @Test
    @Order(4)
    @Story("Update Satellite")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /api/satellites/{id} — обновление спутника")
    @Description("Проверяет обновление существующего спутника")
    void testUpdateSatellite() {
        log.info("Testing PUT update satellite id: {}", createdSatelliteId);

        String body = """
                {
                    "name": "TEST-SAT-001-UPDATED",
                    "type": "COMMUNICATION",
                    "orbitType": "GEO",
                    "status": "INACTIVE",
                    "groupId": null
                }
                """;

        authorized()
                .body(body)
        .when()
                .put("/api/satellites/" + createdSatelliteId)
        .then()
                .statusCode(200)
                .body("name", equalTo("TEST-SAT-001-UPDATED"))
                .body("type", equalTo("COMMUNICATION"))
                .body("status", equalTo("INACTIVE"));
    }

    @Test
    @Order(5)
    @Story("Delete Satellite")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /api/satellites/{id} — удаление спутника")
    @Description("Проверяет удаление существующего спутника")
    void testDeleteSatellite() {
        log.info("Testing DELETE satellite id: {}", createdSatelliteId);

        authorized()
        .when()
                .delete("/api/satellites/" + createdSatelliteId)
        .then()
                .statusCode(204);

        // Verify deleted
        authorized()
        .when()
                .get("/api/satellites/" + createdSatelliteId)
        .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    @Story("Get Satellite")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /api/satellites/{id} — несуществующий спутник (404)")
    @Description("Проверяет корректный ответ при запросе несуществующего спутника")
    void testGetSatelliteNotFound() {
        log.info("Testing GET satellite not found");

        authorized()
        .when()
                .get("/api/satellites/99999")
        .then()
                .statusCode(404);
    }
}
