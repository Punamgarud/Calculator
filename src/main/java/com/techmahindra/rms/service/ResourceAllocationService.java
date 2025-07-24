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
    private ITResourceService resourceService;
    
    @Autowired
    private ProjectService projectService;
    
    public List<ResourceAllocation> findAll() {
        return allocationRepository.findAll();
    }
    
    public Page<ResourceAllocation> findAll(Pageable pageable) {
        return allocationRepository.findAll(pageable);
    }
    
    public Optional<ResourceAllocation> findById(Long id) {
        return allocationRepository.findById(id);
    }
    
    public List<ResourceAllocation> findByEmployee(Employee employee) {
        return allocationRepository.findByEmployee(employee);
    }
    
    public List<ResourceAllocation> findByResource(ITResource resource) {
        return allocationRepository.findByResource(resource);
    }
    
    public List<ResourceAllocation> findByProject(Project project) {
        return allocationRepository.findByProject(project);
    }
    
    public List<ResourceAllocation> findByStatus(ResourceAllocation.AllocationStatus status) {
        return allocationRepository.findByStatus(status);
    }
    
    public List<ResourceAllocation> findActiveAllocationsByEmployee(Employee employee) {
        return allocationRepository.findActiveAllocationsByEmployee(employee);
    }
    
    public List<ResourceAllocation> findActiveAllocationsByResource(ITResource resource) {
        return allocationRepository.findActiveAllocationsByResource(resource);
    }
    
    public List<ResourceAllocation> findActiveAllocationsByProject(Project project) {
        return allocationRepository.findActiveAllocationsByProject(project);
    }
    
    public Page<ResourceAllocation> searchAllocations(String keyword, Pageable pageable) {
        return allocationRepository.findByKeyword(keyword, pageable);
    }
    
    public ResourceAllocation save(ResourceAllocation allocation) {
        validateAllocation(allocation);
        
        // Update resource status if it's being allocated
        if (allocation.getResource() != null && allocation.getStatus() == ResourceAllocation.AllocationStatus.ACTIVE) {
            ITResource resource = allocation.getResource();
            resource.setStatus(ITResource.ResourceStatus.ALLOCATED);
            resourceService.save(resource);
        }
        
        return allocationRepository.save(allocation);
    }
    
    public ResourceAllocation update(Long id, ResourceAllocation allocationDetails) {
        Optional<ResourceAllocation> existingAllocation = allocationRepository.findById(id);
        if (existingAllocation.isPresent()) {
            ResourceAllocation allocation = existingAllocation.get();
            updateAllocationFields(allocation, allocationDetails);
            validateAllocation(allocation);
            return allocationRepository.save(allocation);
        }
        throw new RuntimeException("Allocation not found with id: " + id);
    }
    
    public void deleteById(Long id) {
        Optional<ResourceAllocation> allocation = allocationRepository.findById(id);
        if (allocation.isPresent()) {
            ResourceAllocation alloc = allocation.get();
            
            // Make resource available again if it was allocated
            if (alloc.getResource() != null && alloc.getStatus() == ResourceAllocation.AllocationStatus.ACTIVE) {
                ITResource resource = alloc.getResource();
                resource.setStatus(ITResource.ResourceStatus.AVAILABLE);
                resourceService.save(resource);
            }
            
            allocationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Allocation not found with id: " + id);
        }
    }
    
    public void returnResource(Long allocationId, String returnedBy) {
        Optional<ResourceAllocation> allocation = allocationRepository.findById(allocationId);
        if (allocation.isPresent()) {
            ResourceAllocation alloc = allocation.get();
            alloc.setStatus(ResourceAllocation.AllocationStatus.RETURNED);
            alloc.setActualReturnDate(LocalDate.now());
            alloc.setReturnedBy(returnedBy);
            
            // Make resource available again
            if (alloc.getResource() != null) {
                ITResource resource = alloc.getResource();
                resource.setStatus(ITResource.ResourceStatus.AVAILABLE);
                resourceService.save(resource);
            }
            
            allocationRepository.save(alloc);
        } else {
            throw new RuntimeException("Allocation not found with id: " + allocationId);
        }
    }
    
    public void markAsLost(Long allocationId) {
        Optional<ResourceAllocation> allocation = allocationRepository.findById(allocationId);
        if (allocation.isPresent()) {
            ResourceAllocation alloc = allocation.get();
            alloc.setStatus(ResourceAllocation.AllocationStatus.LOST);
            
            // Mark resource as lost
            if (alloc.getResource() != null) {
                ITResource resource = alloc.getResource();
                resource.setStatus(ITResource.ResourceStatus.LOST);
                resourceService.save(resource);
            }
            
            allocationRepository.save(alloc);
        } else {
            throw new RuntimeException("Allocation not found with id: " + allocationId);
        }
    }
    
    public void cancelAllocation(Long allocationId) {
        Optional<ResourceAllocation> allocation = allocationRepository.findById(allocationId);
        if (allocation.isPresent()) {
            ResourceAllocation alloc = allocation.get();
            alloc.setStatus(ResourceAllocation.AllocationStatus.CANCELLED);
            
            // Make resource available again
            if (alloc.getResource() != null) {
                ITResource resource = alloc.getResource();
                resource.setStatus(ITResource.ResourceStatus.AVAILABLE);
                resourceService.save(resource);
            }
            
            allocationRepository.save(alloc);
        } else {
            throw new RuntimeException("Allocation not found with id: " + allocationId);
        }
    }
    
    public List<ResourceAllocation> getOverdueAllocations() {
        return allocationRepository.findOverdueAllocations(LocalDate.now());
    }
    
    public List<ResourceAllocation> getAllocationsReturningBetween(LocalDate startDate, LocalDate endDate) {
        return allocationRepository.findAllocationsReturningBetween(startDate, endDate);
    }
    
    public List<ResourceAllocation> getAllocationsReturningInNextDays(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        return getAllocationsReturningBetween(startDate, endDate);
    }
    
    public long countByStatus(ResourceAllocation.AllocationStatus status) {
        return allocationRepository.countByStatus(status);
    }
    
    public List<Object[]> getActiveAllocationCountByType() {
        return allocationRepository.countActiveAllocationsByType();
    }
    
    public List<Object[]> getActiveAllocationCountByDepartment() {
        return allocationRepository.countActiveAllocationsByDepartment();
    }
    
    public List<ResourceAllocation> getAllocationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return allocationRepository.findAllocationsBetweenDates(startDate, endDate);
    }
    
    public boolean canAllocateResourceToEmployee(ITResource resource, Employee employee) {
        if (resource == null || employee == null) {
            return false;
        }
        
        // Check if resource is available
        if (resource.getStatus() != ITResource.ResourceStatus.AVAILABLE) {
            return false;
        }
        
        // Check if employee is active
        if (employee.getStatus() != Employee.EmployeeStatus.ACTIVE) {
            return false;
        }
        
        // Check if there's already an active allocation for this resource-employee combination
        long activeAllocations = allocationRepository.countActiveAllocationsForEmployeeAndResource(employee, resource);
        return activeAllocations == 0;
    }
    
    private void validateAllocation(ResourceAllocation allocation) {
        if (allocation.getEmployee() == null) {
            throw new RuntimeException("Employee is required for allocation");
        }
        
        if (allocation.getAllocationDate() == null) {
            throw new RuntimeException("Allocation date is required");
        }
        
        if (allocation.getAllocationType() == null) {
            throw new RuntimeException("Allocation type is required");
        }
        
        // Validate that either resource or project is specified
        if (allocation.getResource() == null && allocation.getProject() == null) {
            throw new RuntimeException("Either resource or project must be specified for allocation");
        }
        
        // Validate date logic
        if (allocation.getExpectedReturnDate() != null && 
            allocation.getAllocationDate().isAfter(allocation.getExpectedReturnDate())) {
            throw new RuntimeException("Expected return date must be after allocation date");
        }
        
        if (allocation.getActualReturnDate() != null && 
            allocation.getAllocationDate().isAfter(allocation.getActualReturnDate())) {
            throw new RuntimeException("Actual return date must be after allocation date");
        }
    }
    
    private void updateAllocationFields(ResourceAllocation existing, ResourceAllocation updated) {
        existing.setEmployee(updated.getEmployee());
        existing.setResource(updated.getResource());
        existing.setProject(updated.getProject());
        existing.setAllocationDate(updated.getAllocationDate());
        existing.setExpectedReturnDate(updated.getExpectedReturnDate());
        existing.setActualReturnDate(updated.getActualReturnDate());
        existing.setStatus(updated.getStatus());
        existing.setAllocationType(updated.getAllocationType());
        existing.setPurpose(updated.getPurpose());
        existing.setNotes(updated.getNotes());
        existing.setAllocatedBy(updated.getAllocatedBy());
        existing.setReturnedBy(updated.getReturnedBy());
    }
}