package com.drony.dronemanager.service;

import com.drony.dronemanager.model.Boundary;
import com.drony.dronemanager.model.Coordinate;
import com.drony.dronemanager.model.Drone;
import com.drony.dronemanager.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    public Drone createDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    public Optional<Drone> getDroneById(Long id) {
        return droneRepository.findById(id);
    }

    public Collection<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    public Optional<Drone> updateDrone(Long id, Drone updatedDrone) {
        var existingDrone = droneRepository.findById(id);
        if (existingDrone.isPresent()) {
            Drone drone = existingDrone.get();
            drone.setName(updatedDrone.getName());
            drone.setDescription(updatedDrone.getDescription());
            drone.setPosition(updatedDrone.getPosition());
            drone.setHeight(updatedDrone.getHeight());
            drone.setSpeed(updatedDrone.getSpeed());

            droneRepository.update(id, drone);
            return Optional.ofNullable(drone);
        } else {
            return Optional.empty();
        }
    }

    public void deleteDrone(Long id) {
        droneRepository.delete(id);
    }
    public Boundary calculateBoundary() {
        var drones = droneRepository.findAll();

        if (drones.isEmpty()) {
            return new Boundary(new Coordinate(0, 0), 0);
        }

        var centroid = drones.stream()
                .map(drone -> new double[] { drone.getPosition().getLatitude(),
                        drone.getPosition().getLongitude() })
                .reduce(new double[2], (a, b) -> new double[] { a[0] + b[0], a[1] + b[1] });

        var centroidX = centroid[0] / drones.size();
        var centroidY = centroid[1] / drones.size();

        var maxDistance = drones.stream()
                .mapToDouble(drone -> Math.sqrt(Math.pow(centroidX -  drone.getPosition().getLatitude(), 2) +
                        Math.pow(centroidY - drone.getPosition().getLongitude(), 2)))
                .max()
                .orElse(0);

        return new Boundary(new Coordinate(centroidX, centroidY), maxDistance);
    }
}