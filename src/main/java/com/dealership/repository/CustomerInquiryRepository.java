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
import org.springframework.transaction.annotation.Transactional;

// data types & structures
import java.util.List; // list of inquiries OR by status



@Repository
public interface CustomerInquiryRepository extends JpaRepository<CustomerInquiry, Long> {

    // Sorted by creation date for Kanban columns
    @Query("SELECT ci FROM CustomerInquiry ci JOIN FETCH ci.vehicle WHERE ci.status = :status ORDER BY ci.createdAt DESC")
    List<CustomerInquiry> findByStatusOrderByCreatedAtDesc(@Param("status") InquiryStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE CustomerInquiry ci SET ci.status = :newStatus, ci.version = ci.version + 1 WHERE ci.id = :id AND ci.version = :version")
    int updateStatusWithOptimisticLock(@Param("id") Long id, 
                                  @Param("newStatus") InquiryStatus newStatus,
                                  @Param("version") Long version);

    // Better performance with JOIN FETCH
    @Query("SELECT ci FROM CustomerInquiry ci JOIN FETCH ci.vehicle WHERE ci.vehicle.id = :vehicleId")
    List<CustomerInquiry> findByVehicleId(@Param("vehicleId") Long vehicleId);
    // new methods
    List<CustomerInquiry> findByCustomerEmail(String customerEmail);
    List<CustomerInquiry> findByCustomerName(String customerName);
}
