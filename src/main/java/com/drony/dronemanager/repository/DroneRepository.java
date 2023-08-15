package com.drony.dronemanager.repository;

import com.drony.dronemanager.model.Drone;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DroneRepository {
    private final AtomicLong counter = new AtomicLong();
    private final ConcurrentMap<Long, Drone> store = new ConcurrentHashMap<>();

    public Drone save(Drone drone) {
        Long id = counter.incrementAndGet();
        drone.setId(id);
        store.put(id, drone);
        return drone;
    }
    public Optional<Drone> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Collection<Drone> findAll() {
        return store.values();
    }

    public Drone update(Long id, Drone drone) {
        if(store.containsKey(id)) {
            drone.setId(id);
            store.put(id, drone);
            return drone;
        }
        return null;
    }

    public void delete(Long id) {
        store.remove(id);
    }


}