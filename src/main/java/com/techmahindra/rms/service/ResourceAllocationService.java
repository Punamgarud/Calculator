package com.techmahindra.rms.service;

import com.techmahindra.rms.model.*;
import com.techmahindra.rms.repository.ResourceAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResourceAllocationService {
    
    @Autowired
    private ResourceAllocationRepository allocationRepository;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private ITResourceService itResourceService;
    
    @Autowired
    private ProjectService projectService;
    
    public List<ResourceAllocation> getAllAllocations() {
        return allocationRepository.findAll();
    }
    
    public Page<ResourceAllocation> getAllAllocationsPageable(Pageable pageable) {
        return allocationRepository.findAll(pageable);
    }
    
    public Optional<ResourceAllocation> getAllocationById(Long id) {
        return allocationRepository.findById(id);
    }
    
    public ResourceAllocation saveAllocation(ResourceAllocation allocation) {
        validateAllocation(allocation);
        
        // Update resource status if IT resource is allocated
        if (allocation.getItResource() != null) {
            ITResource resource = allocation.getItResource();
            resource.setStatus(ITResource.ResourceStatus.ALLOCATED);
            itResourceService.updateResource(resource);
        }
        
        return allocationRepository.save(allocation);
    }
    
    public ResourceAllocation updateAllocation(ResourceAllocation allocation) {
        if (allocation.getId() == null) {
            throw new IllegalArgumentException("Allocation ID cannot be null for update operation");
        }
        validateAllocation(allocation);
        return allocationRepository.save(allocation);
    }
    
    public void deleteAllocation(Long id) {
        Optional<ResourceAllocation> allocationOpt = allocationRepository.findById(id);
        if (allocationOpt.isPresent()) {
            ResourceAllocation allocation = allocationOpt.get();
            
            // Update resource status back to available if IT resource was allocated
            if (allocation.getItResource() != null) {
                ITResource resource = allocation.getItResource();
                resource.setStatus(ITResource.ResourceStatus.AVAILABLE);
                itResourceService.updateResource(resource);
            }
            
            allocationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Allocation not found with ID: " + id);
        }
    }
    
    public List<ResourceAllocation> getAllocationsByEmployee(Employee employee) {
        return allocationRepository.findByEmployee(employee);
    }
    
    public List<ResourceAllocation> getAllocationsByEmployeeId(String employeeId) {
        return allocationRepository.findByEmployeeId(employeeId);
    }
    
    public List<ResourceAllocation> getAllocationsByProject(Project project) {
        return allocationRepository.findByProject(project);
    }
    
    public List<ResourceAllocation> getAllocationsByProjectCode(String projectCode) {
        return allocationRepository.findByProjectCode(projectCode);
    }
    
    public List<ResourceAllocation> getAllocationsByResource(ITResource resource) {
        return allocationRepository.findByItResource(resource);
    }
    
    public List<ResourceAllocation> getAllocationsByResourceCode(String resourceCode) {
        return allocationRepository.findByResourceCode(resourceCode);
    }
    
    public List<ResourceAllocation> getAllocationsByStatus(ResourceAllocation.AllocationStatus status) {
        return allocationRepository.findByStatus(status);
    }
    
    public List<ResourceAllocation> getAllocationsByType(ResourceAllocation.AllocationType type) {
        return allocationRepository.findByType(type);
    }
    
    public List<ResourceAllocation> getActiveAllocations() {
        return allocationRepository.findActiveAllocations();
    }
    
    public List<ResourceAllocation> getOverdueAllocations() {
        return allocationRepository.findOverdueAllocations(LocalDate.now());
    }
    
    public List<ResourceAllocation> getAllocationsEndingSoon(int days) {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(days);
        return allocationRepository.findAllocationsEndingSoon(now, futureDate);
    }
    
    public List<ResourceAllocation> getAllocationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return allocationRepository.findByAllocationDateBetween(startDate, endDate);
    }
    
    public Page<ResourceAllocation> searchAllocations(String keyword, Pageable pageable) {
        return allocationRepository.findByKeyword(keyword, pageable);
    }
    
    public Long getActiveAllocationCountByEmployee(Employee employee) {
        return allocationRepository.countActiveAllocationsByEmployee(employee);
    }
    
    public Long getActiveAllocationCountByProject(Project project) {
        return allocationRepository.countActiveAllocationsByProject(project);
    }
    
    public Long getActiveAllocationCountByResource(ITResource resource) {
        return allocationRepository.countActiveAllocationsByResource(resource);
    }
    
    public Long getAllocationCountByStatus(ResourceAllocation.AllocationStatus status) {
        return allocationRepository.countByStatus(status);
    }
    
    public List<ResourceAllocation> getAllocationsByApprover(String approver) {
        return allocationRepository.findByApprover(approver);
    }
    
    public ResourceAllocation returnAllocation(Long allocationId, String returnedBy) {
        Optional<ResourceAllocation> allocationOpt = getAllocationById(allocationId);
        if (allocationOpt.isPresent()) {
            ResourceAllocation allocation = allocationOpt.get();
            allocation.setActualReturnDate(LocalDate.now());
            allocation.setStatus(ResourceAllocation.AllocationStatus.RETURNED);
            allocation.setNotes(allocation.getNotes() + "\nReturned by: " + returnedBy);
            
            // Update resource status back to available
            if (allocation.getItResource() != null) {
                ITResource resource = allocation.getItResource();
                resource.setStatus(ITResource.ResourceStatus.AVAILABLE);
                itResourceService.updateResource(resource);
            }
            
            return allocationRepository.save(allocation);
        } else {
            throw new IllegalArgumentException("Allocation not found with ID: " + allocationId);
        }
    }
    
    public ResourceAllocation approveAllocation(Long allocationId, String approver) {
        Optional<ResourceAllocation> allocationOpt = getAllocationById(allocationId);
        if (allocationOpt.isPresent()) {
            ResourceAllocation allocation = allocationOpt.get();
            allocation.setStatus(ResourceAllocation.AllocationStatus.ACTIVE);
            allocation.setApprovedBy(approver);
            allocation.setApprovalDate(LocalDate.now());
            return allocationRepository.save(allocation);
        } else {
            throw new IllegalArgumentException("Allocation not found with ID: " + allocationId);
        }
    }
    
    public ResourceAllocation rejectAllocation(Long allocationId, String approver, String reason) {
        Optional<ResourceAllocation> allocationOpt = getAllocationById(allocationId);
        if (allocationOpt.isPresent()) {
            ResourceAllocation allocation = allocationOpt.get();
            allocation.setStatus(ResourceAllocation.AllocationStatus.REJECTED);
            allocation.setApprovedBy(approver);
            allocation.setApprovalDate(LocalDate.now());
            allocation.setNotes(allocation.getNotes() + "\nRejection reason: " + reason);
            
            // Update resource status back to available if IT resource was allocated
            if (allocation.getItResource() != null) {
                ITResource resource = allocation.getItResource();
                resource.setStatus(ITResource.ResourceStatus.AVAILABLE);
                itResourceService.updateResource(resource);
            }
            
            return allocationRepository.save(allocation);
        } else {
            throw new IllegalArgumentException("Allocation not found with ID: " + allocationId);
        }
    }
    
    private void validateAllocation(ResourceAllocation allocation) {
        if (allocation == null) {
            throw new IllegalArgumentException("Allocation cannot be null");
        }
        
        if (allocation.getEmployee() == null) {
            throw new IllegalArgumentException("Employee is required for allocation");
        }
        
        if (allocation.getProject() == null) {
            throw new IllegalArgumentException("Project is required for allocation");
        }
        
        if (allocation.getAllocationDate() == null) {
            throw new IllegalArgumentException("Allocation date is required");
        }
        
        // Validate that the employee exists and is active
        Optional<Employee> employeeOpt = employeeService.getEmployeeById(allocation.getEmployee().getId());
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }
        
        Employee employee = employeeOpt.get();
        if (employee.getStatus() != Employee.EmployeeStatus.ACTIVE) {
            throw new IllegalArgumentException("Employee must be active for allocation");
        }
        
        // Validate that the project exists and is active
        Optional<Project> projectOpt = projectService.getProjectById(allocation.getProject().getId());
        if (projectOpt.isEmpty()) {
            throw new IllegalArgumentException("Project not found");
        }
        
        Project project = projectOpt.get();
        if (project.getStatus() != Project.ProjectStatus.ACTIVE && 
            project.getStatus() != Project.ProjectStatus.PLANNING) {
            throw new IllegalArgumentException("Project must be active or in planning for allocation");
        }
        
        // Validate IT resource if provided
        if (allocation.getItResource() != null) {
            Optional<ITResource> resourceOpt = itResourceService.getResourceById(allocation.getItResource().getId());
            if (resourceOpt.isEmpty()) {
                throw new IllegalArgumentException("IT Resource not found");
            }
            
            ITResource resource = resourceOpt.get();
            if (resource.getStatus() != ITResource.ResourceStatus.AVAILABLE && 
                allocation.getId() == null) { // Allow updates to existing allocations
                throw new IllegalArgumentException("IT Resource must be available for allocation");
            }
        }
        
        // Validate dates
        if (allocation.getExpectedReturnDate() != null && 
            allocation.getAllocationDate().isAfter(allocation.getExpectedReturnDate())) {
            throw new IllegalArgumentException("Allocation date cannot be after expected return date");
        }
        
        if (allocation.getActualReturnDate() != null && 
            allocation.getAllocationDate().isAfter(allocation.getActualReturnDate())) {
            throw new IllegalArgumentException("Allocation date cannot be after actual return date");
        }
        
        // Validate allocation percentage
        if (allocation.getAllocationPercentage() != null && 
            (allocation.getAllocationPercentage() < 1 || allocation.getAllocationPercentage() > 100)) {
            throw new IllegalArgumentException("Allocation percentage must be between 1 and 100");
        }
    }
    
    public long getTotalAllocationCount() {
        return allocationRepository.count();
    }
}