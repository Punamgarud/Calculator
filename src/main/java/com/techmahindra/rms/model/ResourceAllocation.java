package com.techmahindra.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "resource_allocations")
public class ResourceAllocation extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @NotNull(message = "Employee is required")
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "it_resource_id")
    private ITResource itResource;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @NotNull(message = "Project is required")
    private Project project;
    
    @Column(name = "allocation_date", nullable = false)
    @NotNull(message = "Allocation date is required")
    private LocalDate allocationDate;
    
    @Column(name = "expected_return_date")
    private LocalDate expectedReturnDate;
    
    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "allocation_status", nullable = false)
    private AllocationStatus status = AllocationStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "allocation_type", nullable = false)
    private AllocationType type = AllocationType.TEMPORARY;
    
    @Column(name = "allocation_percentage")
    private Integer allocationPercentage = 100;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "approved_by")
    private String approvedBy;
    
    @Column(name = "approval_date")
    private LocalDate approvalDate;
    
    public enum AllocationStatus {
        ACTIVE, RETURNED, OVERDUE, PENDING_APPROVAL, REJECTED
    }
    
    public enum AllocationType {
        PERMANENT, TEMPORARY, SHARED
    }
    
    // Constructors
    public ResourceAllocation() {}
    
    public ResourceAllocation(Employee employee, Project project, LocalDate allocationDate) {
        this.employee = employee;
        this.project = project;
        this.allocationDate = allocationDate;
    }
    
    public ResourceAllocation(Employee employee, ITResource itResource, Project project, 
                            LocalDate allocationDate) {
        this.employee = employee;
        this.itResource = itResource;
        this.project = project;
        this.allocationDate = allocationDate;
    }
    
    // Getters and Setters
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public ITResource getItResource() {
        return itResource;
    }
    
    public void setItResource(ITResource itResource) {
        this.itResource = itResource;
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
    
    public AllocationType getType() {
        return type;
    }
    
    public void setType(AllocationType type) {
        this.type = type;
    }
    
    public Integer getAllocationPercentage() {
        return allocationPercentage;
    }
    
    public void setAllocationPercentage(Integer allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDate getApprovalDate() {
        return approvalDate;
    }
    
    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public boolean isOverdue() {
        return expectedReturnDate != null && 
               actualReturnDate == null && 
               LocalDate.now().isAfter(expectedReturnDate);
    }
}