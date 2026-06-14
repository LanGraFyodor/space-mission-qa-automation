package com.spaceapi.controller;

import com.spaceapi.model.Mission;
import com.spaceapi.repository.MissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private static final Logger log = LoggerFactory.getLogger(MissionController.class);

    private final MissionRepository repository;

    public MissionController(MissionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Mission>> getAll() {
        log.info("Fetching all missions");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mission> getById(@PathVariable Long id) {
        log.info("Fetching mission with id: {}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mission> create(@RequestBody Mission mission) {
        log.info("Creating mission: {}", mission.getName());
        Mission saved = repository.save(mission);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mission> update(@PathVariable Long id, @RequestBody Mission mission) {
        log.info("Updating mission with id: {}", id);
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(mission.getName());
                    existing.setDescription(mission.getDescription());
                    existing.setStatus(mission.getStatus());
                    existing.setLaunchDate(mission.getLaunchDate());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting mission with id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
