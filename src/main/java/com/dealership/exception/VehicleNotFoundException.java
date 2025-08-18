package com.dealership.exception;

// Custom exception for vehicle-specific errors
// RuntimeException class is a superclass of all exceptions in Java
// it comes with a constructor that takes a message
// or a constructor that takes a message and a cause
// or a constructor that takes a message, a cause, and a boolean
public class VehicleNotFoundException extends RuntimeException {
    
    public VehicleNotFoundException(String message) {
        super(message);
    }
    
    public VehicleNotFoundException(Long id) {
        super(String.format("Vehicle with ID %d not found", id));
    }
    
    public VehicleNotFoundException(String field, String value) {
        super(String.format("Vehicle with %s '%s' not found", field, value));
    }
}
