package com.dealership.exception;

// rest
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
// import org.springframework.web.ErrorResponse; // not neceesary, i'm building this class from scratch

// data types & structures
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
        MethodArgumentNotValidException ex, WebRequest request) {
            // Map field errors to a map
            Map<String, String> fieldErrors = new HashMap<>(); 

            // Extract field specific errors using exception handler
            // ok, loosely i see what's happening here: 
            // 1. not sure what a binding result is, but i'm guessing it's a result of the validation
            // 2. the error is an object that contains the field name and the error message
            // 3. the error message is the default message for the field
            // 4. the field name is the name of the field that has the error
            // 5. the fieldErrors map is a map of the field names and the error messages
            // 6. the fieldErrors map is returned to the client
            // 7. the client can then display the error messages to the user
            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                fieldErrors.put(fieldName, errorMessage);
            });

            ErrorResponse errorResponse = new ErrorResponse(
                "Validation failed",
                "One or more fields have invalid values",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getDescription(false),
                fieldErrors
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    // ok pattern is exception handler annotation, response entity, pass in an exception and a web request
    // then we map the field errors to a map
    // then we return a response entity with the error response and the bad request status
    // then we can use this in the controller to return the error response to the client
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVehicleNotFound(
        VehicleNotFoundException ex, WebRequest request) {

            ErrorResponse errorResponse = new ErrorResponse(
                "Vehicle not found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                request.getDescription(false),
                null
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
        IllegalArgumentException ex, WebRequest request) {

            ErrorResponse errorResponse = new ErrorResponse(
                "Invalid argument",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getDescription(false),
                null
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
        Exception ex, WebRequest request) {
            ErrorResponse errorResponse = new ErrorResponse(
                "Internal server error",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                request.getDescription(false),
                null
            );

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
}

// ErrorResponse class
class ErrorResponse {
    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, String> fieldErrors;

    public ErrorResponse(String error, String message, int status, 
                        LocalDateTime timestamp, String path, Map<String, String> fieldErrors) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public Map<String, String> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(Map<String, String> fieldErrors) { this.fieldErrors = fieldErrors; }
}
