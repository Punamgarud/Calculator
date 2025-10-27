package com.techmahindra.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "it_resources")
public class ITResource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Asset tag is required")
    @Column(name = "asset_tag", unique = true, nullable = false)
    private String assetTag;
    
    @NotBlank(message = "Resource name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ResourceCategory category;
    
    @Column(name = "brand")
    private String brand;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "serial_number")
    private String serialNumber;
    
    @Column(name = "specifications", columnDefinition = "TEXT")
    private String specifications;
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "purchase_cost", precision = 10, scale = 2)
    private BigDecimal purchaseCost;
    
    @Column(name = "warranty_expiry")
    private LocalDate warrantyExpiry;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResourceStatus status = ResourceStatus.AVAILABLE;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<ResourceAllocation> allocations;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public ITResource() {}
    
    public ITResource(String assetTag, String name, ResourceCategory category, 
                     String brand, String model) {
        this.assetTag = assetTag;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.status = ResourceStatus.AVAILABLE;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAssetTag() {
        return assetTag;
    }
    
    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ResourceCategory getCategory() {
        return category;
    }
    
    public void setCategory(ResourceCategory category) {
        this.category = category;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getSpecifications() {
        return specifications;
    }
    
    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
    
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
    
    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }
    
    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }
    
    public LocalDate getWarrantyExpiry() {
        return warrantyExpiry;
    }
    
    public void setWarrantyExpiry(LocalDate warrantyExpiry) {
        this.warrantyExpiry = warrantyExpiry;
    }
    
    public ResourceStatus getStatus() {
        return status;
    }
    
    public void setStatus(ResourceStatus status) {
        this.status = status;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<ResourceAllocation> getAllocations() {
        return allocations;
    }
    
    public void setAllocations(List<ResourceAllocation> allocations) {
        this.allocations = allocations;
    }
    
    public boolean isAvailable() {
        return status == ResourceStatus.AVAILABLE;
    }
    
    public enum ResourceCategory {
        LAPTOP("Laptop"),
        DESKTOP("Desktop"),
        MONITOR("Monitor"),
        PRINTER("Printer"),
        MOBILE("Mobile Phone"),
        TABLET("Tablet"),
        SERVER("Server"),
        NETWORK("Network Equipment"),
        SOFTWARE("Software License"),
        OTHER("Other");
        
        private final String displayName;
        
        ResourceCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum ResourceStatus {
        AVAILABLE("Available"),
        ALLOCATED("Allocated"),
        MAINTENANCE("Under Maintenance"),
        RETIRED("Retired"),
        LOST("Lost/Stolen");
        
        private final String displayName;
        
        ResourceStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}