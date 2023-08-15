package com.drony.dronemanager.service;

import com.drony.dronemanager.model.Boundary;
import com.drony.dronemanager.model.Coordinate;
import com.drony.dronemanager.model.Drone;
import com.drony.dronemanager.repository.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DroneServiceTest {

    @InjectMocks
    private DroneService droneService;

    @Mock
    private DroneRepository droneRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateDrone() {
        Drone newDrone = new Drone(null, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);
        Drone savedDrone = new Drone(1L, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);

        when(droneRepository.save(newDrone)).thenReturn(savedDrone);

        Drone result = droneService.createDrone(newDrone);

        assertEquals(savedDrone, result);
    }

    @Test
    public void testGetDroneByIdWhenDroneExists() {
        Drone existingDrone = new Drone(1L, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);

        when(droneRepository.findById(1L)).thenReturn(Optional.of(existingDrone));

        Optional<Drone> result = droneService.getDroneById(1L);

        assertTrue(result.isPresent());
        assertEquals(existingDrone, result.get());
    }

    @Test
    public void testGetDroneByIdWhenDroneDoesNotExist() {
        when(droneRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Drone> result = droneService.getDroneById(1L);

        assertFalse(result.isPresent());
    }


    @Test
    public void testUpdateDroneWhenExists() {
        Drone updatedDrone = new Drone(1L, "Updated Drone", "Updated desc",
                new Coordinate(55.0, 55.0), 105.0, 6.0);

        when(droneRepository.findById(1L)).thenReturn(Optional.of(updatedDrone));
        when(droneRepository.update(1L, updatedDrone)).thenReturn(updatedDrone);

        Optional<Drone> result = droneService.updateDrone(1L, updatedDrone);

        assertTrue(result.isPresent());
        assertEquals(updatedDrone, result.get());
    }

    @Test
    public void testDeleteDrone() {
        doNothing().when(droneRepository).delete(1L);

        droneService.deleteDrone(1L);

        verify(droneRepository, times(1)).delete(1L);
    }

    @Test
    public void testCalculateBoundaries() {
        List<Drone> drones = List.of(
                new Drone(1L, "Drone1", "Desc1", new Coordinate(10.0, 10.0), 100.0, 5.0),
                new Drone(2L, "Drone2", "Desc2", new Coordinate(20.0, 20.0), 105.0, 6.0)
        );

        when(droneRepository.findAll()).thenReturn(drones);

        Boundary result = droneService.calculateBoundary();

        assertEquals(new Coordinate(15.0, 15.0), result.getCenter());
        assertTrue(result.getRadius() > 0);
    }

}