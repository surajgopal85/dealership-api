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
    public Vehicle getVehicleById(@PathVariable Long id) {
        // No need for Optional handling - exception handler will catch VehicleNotFoundException
        return vehicleService.getVehicleById(id);
    }

    // create a new vehicle
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle vehicle) {
        // Verify vehicle exists (will throw exception if not found)
        vehicleService.getVehicleById(id);
        
        // Set the ID and save
        vehicle.setId(id);
        return vehicleService.saveVehicle(vehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        // removed check for vehicle existence because exception handler will catch VehicleNotFoundException
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

    // removed ResponseEntity - not needed because of the exception handler
    @PutMapping("/{id}/sell")
    public Vehicle markAsSold(@PathVariable Long id) {
        // No try-catch needed - removed try-catch because @ControllerAdvice handles exceptions
        return vehicleService.markAsSold(id);
    }

    // get vehicles by status
    @GetMapping("/status/{status}")
    public List<Vehicle> getVehiclesByStatus(@PathVariable VehicleStatus status) {
        return vehicleService.getAllVehicles().stream()
            .filter(v -> v.getStatus() == status)
            .toList();
    }
}
