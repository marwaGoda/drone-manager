package com.drony.dronemanager.service;

import com.drony.dronemanager.entity.DroneEntity;
import com.drony.dronemanager.mapper.DroneMapper;
import com.drony.dronemanager.model.Boundary;
import com.drony.dronemanager.model.Coordinate;
import com.drony.dronemanager.model.DroneDTO;
import com.drony.dronemanager.repository.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DroneServiceTest {

    @InjectMocks
    private DroneService droneService;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneMapper droneMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateDrone() {
        DroneDTO newDroneDTO = new DroneDTO(null, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);

        DroneEntity droneEntity = new DroneEntity(null, "Test Drone", "A sample drone", 50.0, 50.0, 100.0, 5.0);

        DroneEntity savedDroneEntity = new DroneEntity(1L, "Test Drone", "A sample drone", 50.0, 50.0, 100.0, 5.0);

        DroneDTO savedDroneDTO = new DroneDTO(1L, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);

        when(droneMapper.toEntity(newDroneDTO)).thenReturn(droneEntity);
        when(droneRepository.save(droneEntity)).thenReturn(savedDroneEntity);
        when(droneMapper.toDTO(savedDroneEntity)).thenReturn(savedDroneDTO);

        DroneDTO result = droneService.createDrone(newDroneDTO);

        assertEquals(savedDroneDTO, result);
    }

    @Test
    public void testGetDroneByIdWhenDroneExists() {
        DroneEntity existingDroneEntity = new DroneEntity(1L, "Test Drone", "A sample drone",
                50.0, 50.0, 100.0, 5.0);
        DroneDTO existingDroneDTO = new DroneDTO(null, "Test Drone", "A sample drone",
                new Coordinate(50.0, 50.0), 100.0, 5.0);
        when(droneRepository.findById(1L)).thenReturn(Optional.of(existingDroneEntity));
        when(droneMapper.toDTO(existingDroneEntity)).thenReturn(existingDroneDTO);

        Optional<DroneDTO> result = droneService.getDroneById(1L);

        assertTrue(result.isPresent());
        assertEquals(existingDroneDTO, result.get());
    }

    @Test
    public void testGetDroneByIdWhenDroneDoesNotExist() {
        when(droneRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<DroneDTO> result = droneService.getDroneById(1L);

        assertFalse(result.isPresent());
    }
    @Test
    public void testGetAllDrones() {
        List<DroneDTO> expectedDroneDTOS = List.of(
                new DroneDTO(1L, "Drone1", "Desc1", new Coordinate(10.0, 10.0), 100.0, 5.0),
                new DroneDTO(2L, "Drone2", "Desc2", new Coordinate(20.0, 20.0), 105.0, 6.0)
        );

        List<DroneEntity> expectedDroneEntities = List.of(
                new DroneEntity(1L, "Drone1", "Desc1", 10.0, 10.0, 100.0, 5.0),
                new DroneEntity(2L, "Drone2", "Desc2", 20.0, 20.0, 105.0, 6.0)
        );

        when(droneRepository.findAll()).thenReturn(expectedDroneEntities);
        when(droneMapper.toDTO(expectedDroneEntities.get(0))).thenReturn(expectedDroneDTOS.get(0));
        when(droneMapper.toDTO(expectedDroneEntities.get(1))).thenReturn(expectedDroneDTOS.get(1));
        Collection<DroneDTO> result = droneService.getAllDrones();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(expectedDroneDTOS));
    }


    @Test
    public void testUpdateDroneWhenExists() {
        DroneDTO updatedDroneDTO = new DroneDTO(1L, "Updated Drone", "Updated desc",
                new Coordinate(55.0, 55.0), 105.0, 6.0);
        DroneEntity updatedDroneEntity = new DroneEntity(1L, "Updated Drone", "Updated desc",
                55.0, 55.0, 105.0, 6.0);

        when(droneRepository.findById(1L)).thenReturn(Optional.of(updatedDroneEntity));
        when(droneRepository.save(updatedDroneEntity)).thenReturn(updatedDroneEntity);
        when(droneMapper.toDTO(updatedDroneEntity)).thenReturn(updatedDroneDTO);
        Optional<DroneDTO> result = droneService.updateDrone(1L, updatedDroneDTO);

        assertTrue(result.isPresent());
        assertEquals(updatedDroneDTO, result.get());
    }

    @Test
    public void testDeleteDrone() {
        doNothing().when(droneRepository).deleteById(1L);

        droneService.deleteDrone(1L);

        verify(droneRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testCalculateBoundaries() {
        List<DroneDTO> droneDTOS = List.of(
                new DroneDTO(1L, "Drone1", "Desc1", new Coordinate(10.0, 10.0), 100.0, 5.0),
                new DroneDTO(2L, "Drone2", "Desc2", new Coordinate(20.0, 20.0), 105.0, 6.0)
        );
        List<DroneEntity> droneEntities = List.of(
                new DroneEntity(1L, "Drone1", "Desc1", 10.0, 10.0, 100.0, 5.0),
                new DroneEntity(2L, "Drone2", "Desc2", 20.0, 20.0, 105.0, 6.0)
        );

        when(droneRepository.findAll()).thenReturn(droneEntities);

        when(droneMapper.toDTO(droneEntities.get(0))).thenReturn(droneDTOS.get(0));
        when(droneMapper.toDTO(droneEntities.get(1))).thenReturn(droneDTOS.get(1));
        Boundary result = droneService.calculateBoundary();

        assertEquals(new Coordinate(15.0, 15.0), result.getCenter());
        assertTrue(result.getRadius() > 0);
    }

}