package com.dealership.repository;


// models
import com.dealership.model.CustomerInquiry;
import com.dealership.model.InquiryStatus;

// core repository frameworks
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

// data types & structures
import java.util.List; // list of inquiries OR by status



@Repository
public interface CustomerInquiryRepository extends JpaRepository<CustomerInquiry, Long> {
    List<CustomerInquiry> findByStatus(InquiryStatus status);
    List<CustomerInquiry> findByVehicleId(Long vehicleId);
    List<CustomerInquiry> findByCustomerEmail(String customerEmail);
    List<CustomerInquiry> findByCustomerName(String customerName);

    
    
}
