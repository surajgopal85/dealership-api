package com.dealership.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

// attrs: id, customerName, customerEmail, vehicleId, inquiryStatus, notes, createdAt, updatedAt
@Entity
@Table(name = "customer_inquiries", indexes = {
    @Index(name = "idx_vehicle_id", columnList = "vehicle_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_customer_email", columnList = "customerEmail"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
public class CustomerInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Column(nullable = false)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    // because vehicleId is a foreign key, 1 vehicle has many inquiries
    // fetch type lazy means that the vehicle is not fetched until it is needed
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH) 
    @JoinColumn(name = "vehicle_id", nullable = false) // join column is the foreign key
    private Vehicle vehicle;

    // private Long vehicleId; // not sure if this needs something more because it's a foreign key

    @Enumerated(EnumType.STRING) // status is string
    @Column(nullable = false)
    private InquiryStatus status = InquiryStatus.PENDING;

    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // NEW
    @Version
    private Long version;

    // automatically set createdAt and updatedAt on creation
    // removed infavor of @PrePersist method onCreate
    // public CustomerInquiry() {
    //     this.createdAt = LocalDateTime.now();
    //     this.updatedAt = LocalDateTime.now();
    // }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // default constructor
    public CustomerInquiry() {
        
    }

    public CustomerInquiry(String customerName, String customerEmail, Vehicle vehicle,
    String notes, InquiryStatus status) {
        // removed this.onCreate(); handled by @PrePersist
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vehicle = vehicle; // how do i ensure that this foreign key is inserted?
        this.notes = notes;
        this.status = status;
    }

    // getters setters org by attribute
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Long getVehicleId() { return vehicle != null ? vehicle.getId() : null; }
    // this is not needed because we have the vehicle object?
    // needed for DTOs and frontend updates
    public void setVehicleId(Long vehicleId) {
        if (vehicleId != null) {
            Vehicle v = new Vehicle();
            v.setId(vehicleId);
            this.vehicle = v;
        }
    }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public InquiryStatus getStatus() { return status; }
    public void setStatus(InquiryStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // this is needed to update the updatedAt field - do i still need setUpdatedAt?
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("CustomerInquiry{id=%d, customerName=%s, customerEmail=%s, vehicleId=%d, status=%s, notes=%s}", 
        id, customerName, customerEmail, this.getVehicleId(), status, notes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerInquiry)) return false;
        CustomerInquiry that = (CustomerInquiry) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    
}
