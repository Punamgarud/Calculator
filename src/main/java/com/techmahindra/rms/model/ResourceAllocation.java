package com.techmahindra.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "resource_allocations")
public class ResourceAllocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private ITResource resource;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    
    @NotNull(message = "Allocation date is required")
    @Column(name = "allocation_date", nullable = false)
    private LocalDate allocationDate;
    
    @Column(name = "expected_return_date")
    private LocalDate expectedReturnDate;
    
    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AllocationStatus status = AllocationStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "allocation_type")
    private AllocationType allocationType;
    
    @Column(name = "purpose", columnDefinition = "TEXT")
    private String purpose;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "allocated_by")
    private String allocatedBy;
    
    @Column(name = "returned_by")
    private String returnedBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
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
    public ResourceAllocation() {}
    
    public ResourceAllocation(Employee employee, ITResource resource, Project project, 
                            LocalDate allocationDate, AllocationType allocationType) {
        this.employee = employee;
        this.resource = resource;
        this.project = project;
        this.allocationDate = allocationDate;
        this.allocationType = allocationType;
        this.status = AllocationStatus.ACTIVE;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public ITResource getResource() {
        return resource;
    }
    
    public void setResource(ITResource resource) {
        this.resource = resource;
    }
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public LocalDate getAllocationDate() {
        return allocationDate;
    }
    
    public void setAllocationDate(LocalDate allocationDate) {
        this.allocationDate = allocationDate;
    }
    
    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }
    
    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }
    
    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }
    
    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
    
    public AllocationStatus getStatus() {
        return status;
    }
    
    public void setStatus(AllocationStatus status) {
        this.status = status;
    }
    
    public AllocationType getAllocationType() {
        return allocationType;
    }
    
    public void setAllocationType(AllocationType allocationType) {
        this.allocationType = allocationType;
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getAllocatedBy() {
        return allocatedBy;
    }
    
    public void setAllocatedBy(String allocatedBy) {
        this.allocatedBy = allocatedBy;
    }
    
    public String getReturnedBy() {
        return returnedBy;
    }
    
    public void setReturnedBy(String returnedBy) {
        this.returnedBy = returnedBy;
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
    
    public boolean isActive() {
        return status == AllocationStatus.ACTIVE;
    }
    
    public boolean isOverdue() {
        return expectedReturnDate != null && 
               expectedReturnDate.isBefore(LocalDate.now()) && 
               actualReturnDate == null &&
               status == AllocationStatus.ACTIVE;
    }
    
    public enum AllocationStatus {
        ACTIVE("Active"),
        RETURNED("Returned"),
        OVERDUE("Overdue"),
        LOST("Lost"),
        CANCELLED("Cancelled");
        
        private final String displayName;
        
        AllocationStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum AllocationType {
        PERMANENT("Permanent"),
        TEMPORARY("Temporary"),
        PROJECT_SPECIFIC("Project Specific"),
        TRAINING("Training"),
        MAINTENANCE("Maintenance");
        
        private final String displayName;
        
        AllocationType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}