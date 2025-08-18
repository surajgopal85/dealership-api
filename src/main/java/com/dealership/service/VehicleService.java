package com.dealership.service;

// import models
import com.dealership.model.Vehicle;
import com.dealership.model.VehicleStatus;

// import repository
import com.dealership.repository.VehicleRepository;

// import custom exception
import com.dealership.exception.VehicleNotFoundException;

// dep injection
import org.springframework.beans.factory.annotation.Autowired;

// service layer
import org.springframework.stereotype.Service;

// transaction management - need rollback for exceptions
// if a method is transactional, it will be rolled back if an exception is thrown
// if a method is not transactional, it will not be rolled back if an exception is thrown
// if a method is transactional, it will be rolled back if an exception is thrown
import org.springframework.transaction.annotation.Transactional;

// data types & structures
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
@Transactional
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Basic CRUD operations
    // readOnly = true means that the method will not modify the database
    // @transactional annotation is used to mark a method as transactional
    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // add transaction management to the method
    // remove Optional because we're throwing an exception if the vehicle is not found
    // and we're not returning an optional
    @Transactional(readOnly = true)
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    // maintain an optional method because it's used in the controller
    @Transactional
    public Optional<Vehicle> findVehicleById(Long id) {
        validateId(id);
        return vehicleRepository.findById(id);
    }


    public Vehicle saveVehicle(Vehicle vehicle) {
        validateVehicle(vehicle);
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }

    // Business logic
    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE);
    }

    // Get vehicles by make
    public List<Vehicle> getVehiclesByMake(String make) {
        validateMake(make);
        return vehicleRepository.findByMakeIgnoreCase(make.trim());
    }

    public List<Vehicle> getVehiclesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        validatePriceRange(minPrice, maxPrice);
        return vehicleRepository.findByPriceRange(minPrice, maxPrice);
    }

    public Vehicle markAsSold(Long id) {
        Vehicle vehicle = getVehicleById(id); // validates id throws exception if not found
        validateSaleOperation(vehicle);
        vehicle.setStatus(VehicleStatus.SOLD);
        return vehicleRepository.save(vehicle);
    }


    // validation methods are private because scoped to class
    // ===== CENTRALIZED VALIDATION METHODS ===== //
    
    /**
     * Validates vehicle entity for save operations
     */
    private void validateVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        
        // Validate make
        if (vehicle.getMake() == null || vehicle.getMake().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle make cannot be null or empty");
        }
        
        // Validate model  
        if (vehicle.getModel() == null || vehicle.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle model cannot be null or empty");
        }
        
        // Validate year (additional business rules beyond JPA)
        if (vehicle.getYear() != null && vehicle.getYear() > 2026) {
            throw new IllegalArgumentException("Vehicle year cannot be more than 1 year in the future");
        }
        
        // Validate price (business rules)
        if (vehicle.getPrice() != null) {
            if (vehicle.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Vehicle price must be positive");
            }
            if (vehicle.getPrice().compareTo(new BigDecimal("100000")) > 0) {
                throw new IllegalArgumentException("Vehicle price cannot exceed $100,000");
            }
        }
        
        // Validate status
        if (vehicle.getStatus() == null) {
            vehicle.setStatus(VehicleStatus.AVAILABLE); // Set default
        }
    }
    
    /**
     * Validates ID parameter
     */
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Vehicle ID must be positive");
        }
    }
    
    /**
     * Validates make parameter for search operations
     */
    private void validateMake(String make) {
        if (make == null) {
            throw new IllegalArgumentException("Make cannot be null");
        }
        if (make.trim().isEmpty()) {
            throw new IllegalArgumentException("Make cannot be empty");
        }
        if (make.length() > 50) {
            throw new IllegalArgumentException("Make cannot exceed 50 characters");
        }
    }
    
    /**
     * Validates price range parameters
     */
    private void validatePriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range values cannot be null");
        }
        
        if (minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum price cannot be negative");
        }
        
        if (maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Maximum price cannot be negative");
        }
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        
        // Business rule: reasonable price range
        if (maxPrice.compareTo(new BigDecimal("200000")) > 0) {
            throw new IllegalArgumentException("Maximum price search limit is $200,000");
        }
    }
    
    /**
     * Validates business rules for selling a vehicle
     */
    private void validateSaleOperation(Vehicle vehicle) {
        if (vehicle.getStatus() == VehicleStatus.SOLD) {
            throw new IllegalArgumentException(
                String.format("Vehicle with ID %d is already sold", vehicle.getId()));
        }
        
        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE) {
            throw new IllegalArgumentException(
                String.format("Vehicle with ID %d is in maintenance and cannot be sold", vehicle.getId()));
        }
        
        // Additional business rule: price must be set to sell
        if (vehicle.getPrice() == null || vehicle.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot sell vehicle without a valid price");
        }
    }
}
