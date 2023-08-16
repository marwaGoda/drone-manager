package com.drony.dronemanager.controller;


import com.drony.dronemanager.model.Boundary;
import com.drony.dronemanager.model.Coordinate;
import com.drony.dronemanager.model.Drone;
import com.drony.dronemanager.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DroneControllerTest {

    @InjectMocks
    private DroneController droneController;

    @Mock
    private DroneService droneService;

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

        when(droneService.createDrone(newDrone)).thenReturn(savedDrone);

        ResponseEntity<Drone> response = droneController.create(newDrone);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedDrone, response.getBody());
    }
    @Test
    public void testGetAllDrones() {
        List<Drone> expectedDrones = List.of(
                new Drone(1L, "Drone1", "Desc1", new Coordinate(10.0, 10.0), 100.0, 5.0),
                new Drone(2L, "Drone2", "Desc2", new Coordinate(20.0, 20.0), 105.0, 6.0)
        );

        when(droneService.getAllDrones()).thenReturn(expectedDrones);

        ResponseEntity<Collection<Drone>> response = droneController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().containsAll(expectedDrones));
    }

    @Test
    public void testUpdateDroneWhenExists() {
        Drone updatedDrone = new Drone(1L, "Updated Drone", "Updated desc",
                new Coordinate(55.0, 55.0), 105.0, 6.0);

        when(droneService.updateDrone(1L, updatedDrone)).thenReturn(Optional.of(updatedDrone));

        ResponseEntity<Drone> response = droneController.update(1L, updatedDrone);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedDrone, response.getBody());
    }
    @Test
    public void testGetByIdWhenDroneExists() {
        Drone existingDrone = new Drone(1L, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);

        when(droneService.getDroneById(1L)).thenReturn(Optional.of(existingDrone));

        ResponseEntity<Drone> response = droneController.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(existingDrone, response.getBody());
    }

    @Test
    public void testGetByIdWhenDroneDoesNotExist() {
        when(droneService.getDroneById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Drone> response = droneController.getById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteDrone() {
        Drone existingDrone = new Drone(1L, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);
        when(droneService.getDroneById(1L)).thenReturn(Optional.ofNullable(existingDrone));

        ResponseEntity<?> response = droneController.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteDroneWhenNotExists() {
        when(droneService.getDroneById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = droneController.delete(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testCalculateBoundaries() {
        Boundary expectedBoundary = new Boundary(new Coordinate(15.0, 15.0), 7.071);

        when(droneService.calculateBoundary()).thenReturn(expectedBoundary);

        ResponseEntity<Boundary> response = droneController.calculateBoundary();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedBoundary, response.getBody());
    }


}