package com.dealership.service;

// models
import com.dealership.model.CustomerInquiry;
import com.dealership.model.InquiryStatus;

// repository
import com.dealership.repository.CustomerInquiryRepository;

// custom exception
import com.dealership.exception.CustomerInquiryNotFoundException;


// dep injection
import org.springframework.beans.factory.annotation.Autowired;


// service layer
import org.springframework.stereotype.Service;
// transaction mgmt
import org.springframework.transaction.annotation.Transactional;

// data types & structures
import java.util.List;
import java.util.Optional;

// these annotations mark this as a service class with transaction management
// in java, annotations are used to mark a class as a service class or a repository class
@Service
@Transactional
public class CustomerInquiryService {
    private final CustomerInquiryRepository customerInquiryRepository;

    @Autowired
    public CustomerInquiryService(CustomerInquiryRepository customerInquiryRepository) {
        this.customerInquiryRepository = customerInquiryRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerInquiry> getAllCustomerInquiries() {
        return customerInquiryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CustomerInquiry getCustomerInquiryById(Long id) {
        return customerInquiryRepository.findById(id)
            .orElseThrow(() -> new CustomerInquiryNotFoundException(id));
    }

    @Transactional
    public CustomerInquiry saveCustomerInquiry(CustomerInquiry customerInquiry) {
        return customerInquiryRepository.save(customerInquiry);
    }

    public Optional<CustomerInquiry> findCustomerInquiryById(Long id) {
        validateId(id);
        return customerInquiryRepository.findById(id);
    }

    /* private methods */


    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer Inquiry ID cannot be null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Customer Inquiry ID must be positive");
        }
    }
}
