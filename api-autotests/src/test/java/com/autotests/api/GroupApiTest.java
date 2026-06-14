package com.autotests.api;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

@Epic("Space Mission Management API")
@Feature("Groups")
@DisplayName("Group API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupApiTest extends BaseApiTest {

    private static int createdGroupId;

    @Test
    @Order(1)
    @Story("Get Groups")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /api/groups — получение списка группировок")
    @Description("Проверяет получение списка всех спутниковых группировок")
    void testGetAllGroups() {
        log.info("Testing GET all groups");

        authorized()
        .when()
                .get("/api/groups")
        .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    @Order(2)
    @Story("Create Group")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /api/groups — создание группировки")
    @Description("Проверяет создание новой спутниковой группировки")
    void testCreateGroup() {
        log.info("Testing POST create group");

        String body = """
                {
                    "name": "Test Constellation",
                    "purpose": "Automated testing purposes",
                    "missionId": 1
                }
                """;

        createdGroupId = authorized()
                .body(body)
        .when()
                .post("/api/groups")
        .then()
                .statusCode(201)
                .body("name", equalTo("Test Constellation"))
                .body("purpose", equalTo("Automated testing purposes"))
                .body("id", notNullValue())
                .extract()
                .jsonPath().getInt("id");

        log.info("Created group with id: {}", createdGroupId);
    }

    @Test
    @Order(3)
    @Story("Get Group")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /api/groups/{id} — получение группировки по ID")
    @Description("Проверяет получение созданной группировки по её ID")
    void testGetGroupById() {
        log.info("Testing GET group by id: {}", createdGroupId);

        authorized()
        .when()
                .get("/api/groups/" + createdGroupId)
        .then()
                .statusCode(200)
                .body("id", equalTo(createdGroupId))
                .body("name", equalTo("Test Constellation"));
    }

    @Test
    @Order(4)
    @Story("Update Group")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /api/groups/{id} — обновление группировки")
    @Description("Проверяет обновление существующей группировки")
    void testUpdateGroup() {
        log.info("Testing PUT update group id: {}", createdGroupId);

        String body = """
                {
                    "name": "Updated Constellation",
                    "purpose": "Updated purpose",
                    "missionId": 1
                }
                """;

        authorized()
                .body(body)
        .when()
                .put("/api/groups/" + createdGroupId)
        .then()
                .statusCode(200)
                .body("name", equalTo("Updated Constellation"))
                .body("purpose", equalTo("Updated purpose"));
    }

    @Test
    @Order(5)
    @Story("Delete Group")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /api/groups/{id} — удаление группировки")
    @Description("Проверяет удаление существующей группировки")
    void testDeleteGroup() {
        log.info("Testing DELETE group id: {}", createdGroupId);

        authorized()
        .when()
                .delete("/api/groups/" + createdGroupId)
        .then()
                .statusCode(204);

        // Verify deleted
        authorized()
        .when()
                .get("/api/groups/" + createdGroupId)
        .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    @Story("Get Group")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /api/groups/{id} — несуществующая группировка (404)")
    @Description("Проверяет корректный ответ при запросе несуществующей группировки")
    void testGetGroupNotFound() {
        log.info("Testing GET group not found");

        authorized()
        .when()
                .get("/api/groups/99999")
        .then()
                .statusCode(404);
    }
}
