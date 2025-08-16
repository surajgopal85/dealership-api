package com.dealership.controller;

// import models
import com.dealership.model.Vehicle;
import com.dealership.model.VehicleStatus;

// service
import com.dealership.service.VehicleService;

// dependency injection
import org.springframework.beans.factory.annotation.Autowired;

// rest
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

// data types & structures
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {


    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicleOpt = vehicleService.getVehicleById(id);
        return vehicleOpt.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle vehicle) {
        if(!vehicleService.getVehicleById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        vehicle.setId(id);
        Vehicle updatedVehicle = vehicleService.saveVehicle(vehicle);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if(!vehicleService.getVehicleById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }

    @GetMapping("/search")
    public List<Vehicle> searchVehicles(
        @RequestParam(required = false) String make,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice
    ) {
        if(make != null) {
            return vehicleService.getVehiclesByMake(make);
        } else if(minPrice != null && maxPrice != null) {
            return vehicleService.getVehiclesByPriceRange(minPrice, maxPrice);
        } else {
            return vehicleService.getAllVehicles();
        }
    }

    @PutMapping("/{id}/sell")
    public ResponseEntity<Vehicle> markAsSold(@PathVariable Long id) {
        try {
            Vehicle soldVehicle = vehicleService.markAsSold(id);
            return ResponseEntity.ok(soldVehicle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
