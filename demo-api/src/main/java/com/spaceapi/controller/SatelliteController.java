package com.spaceapi.controller;

import com.spaceapi.model.Satellite;
import com.spaceapi.repository.SatelliteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/satellites")
public class SatelliteController {

    private static final Logger log = LoggerFactory.getLogger(SatelliteController.class);

    private final SatelliteRepository repository;

    public SatelliteController(SatelliteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Satellite>> getAll() {
        log.info("Fetching all satellites");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Satellite> getById(@PathVariable Long id) {
        log.info("Fetching satellite with id: {}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Satellite> create(@RequestBody Satellite satellite) {
        log.info("Creating satellite: {}", satellite.getName());
        Satellite saved = repository.save(satellite);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Satellite> update(@PathVariable Long id, @RequestBody Satellite satellite) {
        log.info("Updating satellite with id: {}", id);
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(satellite.getName());
                    existing.setType(satellite.getType());
                    existing.setOrbitType(satellite.getOrbitType());
                    existing.setStatus(satellite.getStatus());
                    existing.setGroupId(satellite.getGroupId());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting satellite with id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
