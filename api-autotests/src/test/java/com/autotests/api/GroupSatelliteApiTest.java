package com.autotests.api;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

@Epic("Space Mission Management API")
@Feature("Group-Satellite Assignment")
@DisplayName("Group Satellite API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupSatelliteApiTest extends BaseApiTest {

    private static int groupId;
    private static int satelliteId;

    @Test
    @Order(1)
    @Story("Setup")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Подготовка: создание группировки для теста")
    @Description("Создаёт группировку, к которой будет привязан спутник")
    void testCreateGroupForAssignment() {
        log.info("Creating group for satellite assignment test");

        String body = """
                {
                    "name": "Assignment Test Group",
                    "purpose": "Test satellite assignment",
                    "missionId": 1
                }
                """;

        groupId = authorized()
                .body(body)
        .when()
                .post("/api/groups")
        .then()
                .statusCode(201)
                .extract()
                .jsonPath().getInt("id");

        log.info("Created group for assignment: {}", groupId);
    }

    @Test
    @Order(2)
    @Story("Setup")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Подготовка: создание спутника для теста")
    @Description("Создаёт спутник, который будет добавлен в группировку")
    void testCreateSatelliteForAssignment() {
        log.info("Creating satellite for assignment test");

        String body = """
                {
                    "name": "ASSIGN-SAT-001",
                    "type": "NAVIGATION",
                    "orbitType": "MEO",
                    "status": "ACTIVE",
                    "groupId": null
                }
                """;

        satelliteId = authorized()
                .body(body)
        .when()
                .post("/api/satellites")
        .then()
                .statusCode(201)
                .extract()
                .jsonPath().getInt("id");

        log.info("Created satellite for assignment: {}", satelliteId);
    }

    @Test
    @Order(3)
    @Story("Add Satellite to Group")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /api/groups/{id}/satellites — добавление спутника в группировку")
    @Description("Проверяет добавление существующего спутника в группировку")
    void testAddSatelliteToGroup() {
        log.info("Testing POST add satellite {} to group {}", satelliteId, groupId);

        authorized()
                .body("{\"satelliteId\": " + satelliteId + "}")
        .when()
                .post("/api/groups/" + groupId + "/satellites")
        .then()
                .statusCode(200)
                .body("groupId", equalTo(groupId))
                .body("id", equalTo(satelliteId));
    }

    @Test
    @Order(4)
    @Story("Verify Assignment")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Проверка: спутник привязан к группировке")
    @Description("Получает спутник по ID и проверяет, что groupId обновился")
    void testVerifySatelliteAssigned() {
        log.info("Verifying satellite {} is assigned to group {}", satelliteId, groupId);

        authorized()
        .when()
                .get("/api/satellites/" + satelliteId)
        .then()
                .statusCode(200)
                .body("groupId", equalTo(groupId));
    }

    @Test
    @Order(5)
    @Story("Add Satellite to Group")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /api/groups/{id}/satellites — несуществующая группировка (404)")
    @Description("Проверяет ошибку при добавлении спутника в несуществующую группировку")
    void testAddSatelliteToNonExistentGroup() {
        log.info("Testing add satellite to non-existent group");

        authorized()
                .body("{\"satelliteId\": " + satelliteId + "}")
        .when()
                .post("/api/groups/99999/satellites")
        .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    @Story("Add Satellite to Group")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /api/groups/{id}/satellites — несуществующий спутник (400)")
    @Description("Проверяет ошибку при добавлении несуществующего спутника в группировку")
    void testAddNonExistentSatelliteToGroup() {
        log.info("Testing add non-existent satellite to group");

        authorized()
                .body("{\"satelliteId\": 99999}")
        .when()
                .post("/api/groups/" + groupId + "/satellites")
        .then()
                .statusCode(400);
    }
}
