package com.dealership.exception;

public class CustomerInquiryNotFoundException extends RuntimeException {

    public CustomerInquiryNotFoundException(String message) {
        super(message);
    }

    public CustomerInquiryNotFoundException(Long id) {
        super(String.format("Customer inquiry with ID %d not found", id));
    }

    public CustomerInquiryNotFoundException(String field, String value) {
        super(String.format("Customer inquiry with %s '%s' not found", field, value));
    }
}
