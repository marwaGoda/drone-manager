package com.drony.dronemanager.controller;

import com.drony.dronemanager.model.Boundary;
import com.drony.dronemanager.model.Drone;
import com.drony.dronemanager.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;

    @PostMapping
    public ResponseEntity<Drone> create(@RequestBody Drone drone) {
        return  new ResponseEntity<>(droneService.createDrone(drone), HttpStatus.CREATED);
    }

    @GetMapping("/boundary")
    public ResponseEntity<Boundary> calculateBoundary() {
        return ResponseEntity.ok(droneService.calculateBoundary());
    }
   @GetMapping
   public ResponseEntity<Collection<Drone>>  getAll() {
        return new ResponseEntity<>(droneService.getAllDrones(), HttpStatus.OK);
   }

    @GetMapping("/{id}")
    public ResponseEntity<Drone>  getById(@PathVariable Long id) {
        var drone = droneService.getDroneById(id);
        return drone.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drone> update(@PathVariable Long id, @RequestBody Drone drone) {
        var updatedDrone = droneService.updateDrone(id, drone);
        return updatedDrone.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var drone = droneService.getDroneById(id);
        if (drone.isPresent()) {
            droneService.deleteDrone(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}