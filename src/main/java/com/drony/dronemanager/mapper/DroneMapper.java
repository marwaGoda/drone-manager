package com.drony.dronemanager.mapper;

import com.drony.dronemanager.entity.DroneEntity;
import com.drony.dronemanager.model.Coordinate;
import com.drony.dronemanager.model.DroneDTO;
import org.springframework.stereotype.Component;

@Component
public class DroneMapper {

    public DroneDTO toDTO(DroneEntity entity) {
        Coordinate coordinate = new Coordinate(entity.getLatitude(), entity.getLongitude());
        return new DroneDTO(entity.getId(), entity.getName(), entity.getDescription(),
                coordinate, entity.getHeight(), entity.getSpeed());
    }
    public DroneEntity toEntity(DroneDTO dto) {
        DroneEntity entity = new DroneEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setLatitude(dto.getPosition().getLatitude());
        entity.setLongitude(dto.getPosition().getLongitude());
        entity.setHeight(dto.getHeight());
        entity.setSpeed(dto.getSpeed());
        return entity;
    }
}
