package com.dealership.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/vehicles")

public class VehicleController {
    
    @GetMapping
    public List<String> getAllVehicles() {
        List<String> vehicles = new ArrayList<>();
        vehicles.add("Toyota Camry 2023");
        vehicles.add("Honda Accord 2024");
        vehicles.add("BMW X5 2023");
        
        return vehicles;
    }
}
