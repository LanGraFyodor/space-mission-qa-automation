package com.spaceapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mission name is required")
    @Column(nullable = false)
    private String name;

    private String description;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;

    private String launchDate;

    public Mission() {
    }

    public Mission(String name, String description, String status, String launchDate) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.launchDate = launchDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }
}
