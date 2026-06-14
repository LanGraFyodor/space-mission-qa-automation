package com.loadtests;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Нагрузочный тест: Полный цикл работы оператора космической миссии.
 *
 * Пользовательский сценарий:
 * 1. Авторизация (POST /api/auth/login)
 * 2. Получение сведений о миссиях (GET /api/missions)
 * 3. Создание новой группировки (POST /api/groups)
 * 4. Получение сведений о группировке (GET /api/groups/{id})
 * 5. Создание спутника (POST /api/satellites)
 * 6. Добавление спутника в группировку (POST /api/groups/{id}/satellites)
 * 7. Получение обновлённой группировки (GET /api/groups/{id})
 * 8. Обновление спутника (PUT /api/satellites/{id})
 * 9. Удаление спутника (DELETE /api/satellites/{id})
 *
 * Профиль нагрузки:
 * - Ramp-up: от 0 до 50 пользователей за 30 секунд
 * - Steady load: 50 пользователей на протяжении 60 секунд
 * - Ramp-down: постепенное снижение за 10 секунд
 * - Общая длительность: ~100 секунд (≥ 60 секунд)
 *
 * Тип действий: комбинированный — чтение (GET) и запись (POST/PUT/DELETE)
 */
public class SpaceMissionSimulation extends Simulation {

    // Feeder to generate unique data for each virtual user
    private static final Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random rnd = new Random();
                String uid = UUID.randomUUID().toString().substring(0, 8);
                return Map.of(
                        "username", "loaduser_" + uid,
                        "groupName", "LoadGroup-" + uid,
                        "satName", "LOAD-SAT-" + uid,
                        "orbitType", new String[]{"LEO", "MEO", "GEO"}[rnd.nextInt(3)],
                        "satType", new String[]{"COMMUNICATION", "OBSERVATION", "NAVIGATION"}[rnd.nextInt(3)]
                );
            }).iterator();

    // HTTP Protocol Configuration
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // Full user scenario
    ScenarioBuilder operatorScenario = scenario("Полный цикл оператора космической миссии")
            .feed(feeder)

            // 1. Авторизация
            .exec(
                    http("1. Авторизация")
                            .post("/api/auth/login")
                            .body(StringBody("{\"username\": \"#{username}\", \"password\": \"password\"}"))
                            .check(status().is(200))
                            .check(jsonPath("$.token").saveAs("authToken"))
            )
            .pause(Duration.ofMillis(500), Duration.ofSeconds(1))

            // 2. Получение сведений о миссиях (GET - чтение)
            .exec(
                    http("2. Получение списка миссий")
                            .get("/api/missions")
                            .header("Authorization", "Bearer #{authToken}")
                            .check(status().is(200))
                            .check(jsonPath("$[0].id").saveAs("missionId"))
            )
            .pause(Duration.ofMillis(300), Duration.ofMillis(800))

            // 3. Создание новой группировки (POST - запись)
            .exec(
                    http("3. Создание группировки")
                            .post("/api/groups")
                            .header("Authorization", "Bearer #{authToken}")
                            .body(StringBody(
                                    "{\"name\": \"#{groupName}\", \"purpose\": \"Load test group\", \"missionId\": #{missionId}}"
                            ))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("groupId"))
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(600))

            // 4. Получение сведений о группировке (GET - чтение)
            .exec(
                    http("4. Получение группировки")
                            .get("/api/groups/#{groupId}")
                            .header("Authorization", "Bearer #{authToken}")
                            .check(status().is(200))
                            .check(jsonPath("$.name").is("#{groupName}"))
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(500))

            // 5. Создание спутника (POST - запись)
            .exec(
                    http("5. Создание спутника")
                            .post("/api/satellites")
                            .header("Authorization", "Bearer #{authToken}")
                            .body(StringBody(
                                    "{\"name\": \"#{satName}\", \"type\": \"#{satType}\", \"orbitType\": \"#{orbitType}\", \"status\": \"ACTIVE\", \"groupId\": null}"
                            ))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("satelliteId"))
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(500))

            // 6. Добавление спутника в группировку (POST - запись)
            .exec(
                    http("6. Привязка спутника")
                            .post("/api/groups/#{groupId}/satellites")
                            .header("Authorization", "Bearer #{authToken}")
                            .body(StringBody("{ \"satelliteId\": #{satelliteId} }"))
                            .check(status().is(200))
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(500))

            // 7. Получение обновлённой группировки (GET - чтение)
            .exec(
                    http("7. Получение группировки со спутниками")
                            .get("/api/groups/#{groupId}")
                            .header("Authorization", "Bearer #{authToken}")
                            .check(status().is(200))
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(500))

            // 8. Обновление спутника (PUT - запись/изменение)
            .exec(
                    http("8. Обновление спутника")
                            .put("/api/satellites/#{satelliteId}")
                            .header("Authorization", "Bearer #{authToken}")
                            .body(StringBody(
                                    "{\"name\": \"#{satName}-UPD\", \"type\": \"#{satType}\", \"orbitType\": \"#{orbitType}\", \"status\": \"INACTIVE\", \"groupId\": #{groupId}}"
                            ))
                            .check(status().is(200))
            )
            .pause(Duration.ofMillis(200), Duration.ofMillis(500))

            // 9. Удаление спутника (DELETE - запись/удаление)
            .exec(
                    http("9. Удаление спутника")
                            .delete("/api/satellites/#{satelliteId}")
                            .header("Authorization", "Bearer #{authToken}")
                            .check(status().is(204))
            );

    // Load Profile
    {
        setUp(
                operatorScenario.injectOpen(
                        // Ramp-up: 0 → 50 users over 30 seconds
                        rampUsers(50).during(Duration.ofSeconds(30)),
                        // Steady load: constant 50 users for 60 seconds
                        constantUsersPerSec(2).during(Duration.ofSeconds(60)).randomized(),
                        // Ramp-down: quick cooldown
                        rampUsers(10).during(Duration.ofSeconds(10))
                )
        ).protocols(httpProtocol)
         .assertions(
                 global().responseTime().max().lt(5000),
                 global().successfulRequests().percent().gt(95.0)
         );
    }
}
