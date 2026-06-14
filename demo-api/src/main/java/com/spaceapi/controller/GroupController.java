package com.spaceapi.controller;

import com.spaceapi.dto.AddSatelliteRequest;
import com.spaceapi.model.SatelliteGroup;
import com.spaceapi.repository.SatelliteGroupRepository;
import com.spaceapi.repository.SatelliteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private static final Logger log = LoggerFactory.getLogger(GroupController.class);

    private final SatelliteGroupRepository groupRepository;
    private final SatelliteRepository satelliteRepository;

    public GroupController(SatelliteGroupRepository groupRepository,
                           SatelliteRepository satelliteRepository) {
        this.groupRepository = groupRepository;
        this.satelliteRepository = satelliteRepository;
    }

    @GetMapping
    public ResponseEntity<List<SatelliteGroup>> getAll() {
        log.info("Fetching all groups");
        return ResponseEntity.ok(groupRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SatelliteGroup> getById(@PathVariable Long id) {
        log.info("Fetching group with id: {}", id);
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SatelliteGroup> create(@RequestBody SatelliteGroup group) {
        log.info("Creating group: {}", group.getName());
        SatelliteGroup saved = groupRepository.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SatelliteGroup> update(@PathVariable Long id, @RequestBody SatelliteGroup group) {
        log.info("Updating group with id: {}", id);
        return groupRepository.findById(id)
                .map(existing -> {
                    existing.setName(group.getName());
                    existing.setPurpose(group.getPurpose());
                    existing.setMissionId(group.getMissionId());
                    return ResponseEntity.ok(groupRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting group with id: {}", id);
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/satellites")
    public ResponseEntity<?> addSatellite(@PathVariable Long id,
                                          @RequestBody AddSatelliteRequest request) {
        log.info("Adding satellite {} to group {}", request.getSatelliteId(), id);

        var groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var satelliteOpt = satelliteRepository.findById(request.getSatelliteId());
        if (satelliteOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Satellite not found with id: " + request.getSatelliteId()));
        }

        var satellite = satelliteOpt.get();
        satellite.setGroupId(id);
        satelliteRepository.save(satellite);

        log.info("Satellite {} added to group {}", request.getSatelliteId(), id);
        return ResponseEntity.ok(satellite);
    }
}
