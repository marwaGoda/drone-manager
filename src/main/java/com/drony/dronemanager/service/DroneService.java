package com.drony.dronemanager.service;

import com.drony.dronemanager.entity.DroneEntity;
import com.drony.dronemanager.mapper.DroneMapper;
import com.drony.dronemanager.model.Boundary;
import com.drony.dronemanager.model.Coordinate;
import com.drony.dronemanager.model.DroneDTO;
import com.drony.dronemanager.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private DroneMapper droneMapper;

    public DroneDTO createDrone(DroneDTO droneDto) {
        DroneEntity entity = droneMapper.toEntity(droneDto);
        DroneEntity savedEntity = droneRepository.save(entity);
        return droneMapper.toDTO(savedEntity);
    }

    public Optional<DroneDTO> getDroneById(Long id) {
        return droneRepository.findById(id)
                .map(droneMapper::toDTO);
    }

    public List<DroneDTO> getAllDrones() {
        return droneRepository.findAll().stream()
                .map(droneMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<DroneDTO> updateDrone(Long id, DroneDTO updatedDroneDTO) {
        var existingDroneEntity = droneRepository.findById(id);
        if (existingDroneEntity.isPresent()) {
            DroneEntity entityToUpdate = existingDroneEntity.get();
            entityToUpdate.setName(updatedDroneDTO.getName());
            entityToUpdate.setDescription(updatedDroneDTO.getDescription());
            entityToUpdate.setLatitude(updatedDroneDTO.getPosition().getLatitude());
            entityToUpdate.setLongitude(updatedDroneDTO.getPosition().getLongitude());
            entityToUpdate.setHeight(updatedDroneDTO.getHeight());
            entityToUpdate.setSpeed(updatedDroneDTO.getSpeed());

            DroneEntity updatedEntity = droneRepository.save(entityToUpdate);
            return Optional.of(droneMapper.toDTO(updatedEntity));
        } else {
            return Optional.empty();
        }
    }

    public void deleteDrone(Long id) {
        droneRepository.deleteById(id);
    }
    public Boundary calculateBoundary() {
        var drones = getAllDrones();

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