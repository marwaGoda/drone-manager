package com.drony.dronemanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DroneDTO {
    private Long id;
    private String name;
    private String description;
    private Coordinate position;
    private double height;
    private double speed;
}