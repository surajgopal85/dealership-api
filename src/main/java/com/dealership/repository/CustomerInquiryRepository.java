package com.dealership.repository;


// models
import com.dealership.model.CustomerInquiry;
import com.dealership.model.InquiryStatus;

// core repository frameworks
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

// data types & structures
import java.util.List; // list of inquiries OR by status



@Repository
public interface CustomerInquiryRepository extends JpaRepository<CustomerInquiry, Long> {

    // this is the same as the findByStatus method in the VehicleRepository
    // but it is more explicit and allows for the use of @Param to bind the status parameter
    @Query("SELECT ci FROM CustomerInquiry ci WHERE ci.status = :status")
    List<CustomerInquiry> findByStatus(@Param("status") InquiryStatus status);

    // Sorted by creation date for Kanban columns
    @Query("SELECT ci FROM CustomerInquiry ci JOIN FETCH ci.vehicle WHERE ci.status = :status ORDER BY ci.createdAt DESC")
    List<CustomerInquiry> findByStatusOrderByCreatedAtDesc(@Param("status") InquiryStatus status);

    @Modifying
    @Query("UPDATE CustomerInquiry ci SET ci.status = :newStatus, ci.version = ci.version + 1 WHERE ci.id = :id AND ci.version = :version")
    int updateStatusWithOptimisticLock(@Param("id") Long id, 
                                  @Param("newStatus") InquiryStatus newStatus,
                                  @Param("version") Long version);

    List<CustomerInquiry> findByVehicleId(Long vehicleId);
    List<CustomerInquiry> findByCustomerEmail(String customerEmail);
    List<CustomerInquiry> findByCustomerName(String customerName);

    
    
}
