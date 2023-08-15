package com.drony.dronemanager.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Boundary {
    private Coordinate center;
    private double radius;
}

