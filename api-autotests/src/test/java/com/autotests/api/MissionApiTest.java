package com.autotests.api;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

@Epic("Space Mission Management API")
@Feature("Missions")
@DisplayName("Mission API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MissionApiTest extends BaseApiTest {

    private static int createdMissionId;

    @Test
    @Order(1)
    @Story("Get Missions")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /api/missions — получение списка миссий")
    @Description("Проверяет получение списка всех миссий (включая предзагруженные данные)")
    void testGetAllMissions() {
        log.info("Testing GET all missions");

        authorized()
        .when()
                .get("/api/missions")
        .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(1)))
                .body("[0].name", notNullValue());
    }

    @Test
    @Order(2)
    @Story("Create Mission")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /api/missions — создание миссии")
    @Description("Проверяет создание новой миссии и получение корректного ответа")
    void testCreateMission() {
        log.info("Testing POST create mission");

        String body = """
                {
                    "name": "Test Mission Alpha",
                    "description": "Automated test mission",
                    "status": "PLANNED",
                    "launchDate": "2025-12-01"
                }
                """;

        createdMissionId = authorized()
                .body(body)
        .when()
                .post("/api/missions")
        .then()
                .statusCode(201)
                .body("name", equalTo("Test Mission Alpha"))
                .body("status", equalTo("PLANNED"))
                .body("id", notNullValue())
                .extract()
                .jsonPath().getInt("id");

        log.info("Created mission with id: {}", createdMissionId);
    }

    @Test
    @Order(3)
    @Story("Get Mission")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /api/missions/{id} — получение миссии по ID")
    @Description("Проверяет получение созданной миссии по её ID")
    void testGetMissionById() {
        log.info("Testing GET mission by id: {}", createdMissionId);

        authorized()
        .when()
                .get("/api/missions/" + createdMissionId)
        .then()
                .statusCode(200)
                .body("id", equalTo(createdMissionId))
                .body("name", equalTo("Test Mission Alpha"));
    }

    @Test
    @Order(4)
    @Story("Update Mission")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /api/missions/{id} — обновление миссии")
    @Description("Проверяет обновление существующей миссии")
    void testUpdateMission() {
        log.info("Testing PUT update mission id: {}", createdMissionId);

        String body = """
                {
                    "name": "Test Mission Alpha Updated",
                    "description": "Updated description",
                    "status": "ACTIVE",
                    "launchDate": "2025-12-15"
                }
                """;

        authorized()
                .body(body)
        .when()
                .put("/api/missions/" + createdMissionId)
        .then()
                .statusCode(200)
                .body("name", equalTo("Test Mission Alpha Updated"))
                .body("status", equalTo("ACTIVE"));
    }

    @Test
    @Order(5)
    @Story("Delete Mission")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /api/missions/{id} — удаление миссии")
    @Description("Проверяет удаление существующей миссии")
    void testDeleteMission() {
        log.info("Testing DELETE mission id: {}", createdMissionId);

        authorized()
        .when()
                .delete("/api/missions/" + createdMissionId)
        .then()
                .statusCode(204);

        // Verify mission is deleted
        authorized()
        .when()
                .get("/api/missions/" + createdMissionId)
        .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    @Story("Get Mission")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /api/missions/{id} — несуществующая миссия (404)")
    @Description("Проверяет корректный ответ при запросе несуществующей миссии")
    void testGetMissionNotFound() {
        log.info("Testing GET mission not found");

        authorized()
        .when()
                .get("/api/missions/99999")
        .then()
                .statusCode(404);
    }
}
