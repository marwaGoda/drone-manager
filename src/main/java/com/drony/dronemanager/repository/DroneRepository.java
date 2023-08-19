package com.drony.dronemanager.repository;

import com.drony.dronemanager.entity.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<DroneEntity, Long> {
}
