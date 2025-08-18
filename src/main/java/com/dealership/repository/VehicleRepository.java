package com.dealership.repository;

// import current vehicle models
import com.dealership.model.Vehicle;
import com.dealership.model.VehicleStatus;

// core repository frameworks
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// data types & structures
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByMakeIgnoreCase(String make);
    List<Vehicle> findByModel(String model);
    List<Vehicle> findByYear(Integer year);
    List<Vehicle> findByPrice(BigDecimal price);
    List<Vehicle> findByStatus(VehicleStatus status);

    @Query("SELECT v FROM Vehicle v WHERE v.make = :make AND v.status = :status")
    List<Vehicle> findByMakeAndStatus(@Param("make") String make, @Param("status") VehicleStatus status);

    @Query("SELECT v FROM Vehicle v WHERE v.price BETWEEN :minPrice AND :maxPrice ORDER BY v.price ASC")
    List<Vehicle> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

}
