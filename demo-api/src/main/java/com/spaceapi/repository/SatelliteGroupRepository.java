package com.spaceapi.repository;

import com.spaceapi.model.SatelliteGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatelliteGroupRepository extends JpaRepository<SatelliteGroup, Long> {
}
