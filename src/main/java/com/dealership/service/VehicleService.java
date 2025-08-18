package com.dealership.service;

// import models
import com.dealership.model.Vehicle;
import com.dealership.model.VehicleStatus;

// import repository
import com.dealership.repository.VehicleRepository;

// dep injection
import org.springframework.beans.factory.annotation.Autowired;

// service layer
import org.springframework.stereotype.Service;

// data types & structures
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Basic CRUD operations
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    // Business logic
    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);
    }

    public List<Vehicle> getVehiclesByMake(String make) {
        return vehicleRepository.findByMakeIgnoreCase(make);
    }

    public List<Vehicle> getVehiclesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return vehicleRepository.findByPriceRange(minPrice, maxPrice);
    }

    public Vehicle markAsSold(Long id) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        if(vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setStatus(VehicleStatus.SOLD);
            return vehicleRepository.save(vehicle);
        }
        throw new RuntimeException("Vehicle not found with id " + id);
    }
}
