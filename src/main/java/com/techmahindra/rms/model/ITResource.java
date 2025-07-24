package com.techmahindra.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "it_resources")
public class ITResource extends BaseEntity {
    
    @NotBlank(message = "Resource code is required")
    @Column(name = "resource_code", unique = true, nullable = false)
    private String resourceCode;
    
    @NotBlank(message = "Resource name is required")
    @Column(name = "resource_name", nullable = false)
    private String resourceName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_category", nullable = false)
    private ResourceCategory category;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "brand")
    private String brand;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "serial_number")
    private String serialNumber;
    
    @DecimalMin(value = "0.0", message = "Purchase cost must be positive")
    @Column(name = "purchase_cost", precision = 10, scale = 2)
    private BigDecimal purchaseCost;
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "warranty_expiry_date")
    private LocalDate warrantyExpiryDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_status", nullable = false)
    private ResourceStatus status = ResourceStatus.AVAILABLE;
    
    @Column(name = "vendor")
    private String vendor;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "specifications", length = 2000)
    private String specifications;
    
    @OneToMany(mappedBy = "itResource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResourceAllocation> resourceAllocations = new ArrayList<>();
    
    public enum ResourceType {
        HARDWARE, SOFTWARE, NETWORK, STORAGE, SECURITY, CLOUD
    }
    
    public enum ResourceCategory {
        LAPTOP, DESKTOP, SERVER, MOBILE, TABLET, PRINTER, SCANNER, 
        PROJECTOR, MONITOR, KEYBOARD, MOUSE, HEADSET, WEBCAM,
        OPERATING_SYSTEM, APPLICATION_SOFTWARE, DEVELOPMENT_TOOL,
        DATABASE, MIDDLEWARE, ANTIVIRUS, FIREWALL, ROUTER, SWITCH,
        STORAGE_DEVICE, CLOUD_SERVICE, LICENSE
    }
    
    public enum ResourceStatus {
        AVAILABLE, ALLOCATED, MAINTENANCE, RETIRED, LOST, DAMAGED
    }
    
    // Constructors
    public ITResource() {}
    
    public ITResource(String resourceCode, String resourceName, ResourceType resourceType, 
                     ResourceCategory category) {
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.category = category;
    }
    
    // Getters and Setters
    public String getResourceCode() {
        return resourceCode;
    }
    
    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    public ResourceType getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }
    
    public ResourceCategory getCategory() {
        return category;
    }
    
    public void setCategory(ResourceCategory category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }
    
    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }
    
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
    
    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    public LocalDate getWarrantyExpiryDate() {
        return warrantyExpiryDate;
    }
    
    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
        this.warrantyExpiryDate = warrantyExpiryDate;
    }
    
    public ResourceStatus getStatus() {
        return status;
    }
    
    public void setStatus(ResourceStatus status) {
        this.status = status;
    }
    
    public String getVendor() {
        return vendor;
    }
    
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getSpecifications() {
        return specifications;
    }
    
    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
    
    public List<ResourceAllocation> getResourceAllocations() {
        return resourceAllocations;
    }
    
    public void setResourceAllocations(List<ResourceAllocation> resourceAllocations) {
        this.resourceAllocations = resourceAllocations;
    }
}