package com.spaceapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "satellites")
public class Satellite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Satellite name is required")
    @Column(nullable = false)
    private String name;

    private String type;

    private String orbitType;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;

    private Long groupId;

    public Satellite() {
    }

    public Satellite(String name, String type, String orbitType, String status, Long groupId) {
        this.name = name;
        this.type = type;
        this.orbitType = orbitType;
        this.status = status;
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrbitType() {
        return orbitType;
    }

    public void setOrbitType(String orbitType) {
        this.orbitType = orbitType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
