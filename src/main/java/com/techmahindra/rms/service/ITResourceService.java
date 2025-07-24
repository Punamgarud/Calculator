package com.techmahindra.rms.service;

import com.techmahindra.rms.model.ITResource;
import com.techmahindra.rms.repository.ITResourceRepository;
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
public class ITResourceService {
    
    @Autowired
    private ITResourceRepository itResourceRepository;
    
    public List<ITResource> getAllResources() {
        return itResourceRepository.findAll();
    }
    
    public Page<ITResource> getAllResourcesPageable(Pageable pageable) {
        return itResourceRepository.findAll(pageable);
    }
    
    public Optional<ITResource> getResourceById(Long id) {
        return itResourceRepository.findById(id);
    }
    
    public Optional<ITResource> getResourceByCode(String resourceCode) {
        return itResourceRepository.findByResourceCode(resourceCode);
    }
    
    public ITResource saveResource(ITResource resource) {
        validateResource(resource);
        return itResourceRepository.save(resource);
    }
    
    public ITResource updateResource(ITResource resource) {
        if (resource.getId() == null) {
            throw new IllegalArgumentException("Resource ID cannot be null for update operation");
        }
        validateResource(resource);
        return itResourceRepository.save(resource);
    }
    
    public void deleteResource(Long id) {
        if (!itResourceRepository.existsById(id)) {
            throw new IllegalArgumentException("Resource not found with ID: " + id);
        }
        itResourceRepository.deleteById(id);
    }
    
    public List<ITResource> getResourcesByType(ITResource.ResourceType resourceType) {
        return itResourceRepository.findByResourceType(resourceType);
    }
    
    public List<ITResource> getResourcesByCategory(ITResource.ResourceCategory category) {
        return itResourceRepository.findByCategory(category);
    }
    
    public List<ITResource> getResourcesByStatus(ITResource.ResourceStatus status) {
        return itResourceRepository.findByStatus(status);
    }
    
    public List<ITResource> getResourcesByTypeAndStatus(ITResource.ResourceType resourceType, 
                                                       ITResource.ResourceStatus status) {
        return itResourceRepository.findByResourceTypeAndStatus(resourceType, status);
    }
    
    public List<ITResource> getResourcesByCategoryAndStatus(ITResource.ResourceCategory category, 
                                                           ITResource.ResourceStatus status) {
        return itResourceRepository.findByCategoryAndStatus(category, status);
    }
    
    public List<ITResource> getResourcesByBrand(String brand) {
        return itResourceRepository.findByBrand(brand);
    }
    
    public List<ITResource> getResourcesByVendor(String vendor) {
        return itResourceRepository.findByVendor(vendor);
    }
    
    public List<ITResource> getResourcesByLocation(String location) {
        return itResourceRepository.findByLocation(location);
    }
    
    public Page<ITResource> searchResources(String keyword, Pageable pageable) {
        return itResourceRepository.findByKeyword(keyword, pageable);
    }
    
    public List<ITResource> getAvailableResources() {
        return itResourceRepository.findAvailableResources();
    }
    
    public List<ITResource> getAllocatedResources() {
        return itResourceRepository.findAllocatedResources();
    }
    
    public List<ITResource> getResourcesWithWarrantyExpiringBetween(LocalDate startDate, LocalDate endDate) {
        return itResourceRepository.findByWarrantyExpiryDateBetween(startDate, endDate);
    }
    
    public List<ITResource> getExpiredWarrantyResources() {
        return itResourceRepository.findExpiredWarrantyResources(LocalDate.now());
    }
    
    public List<ITResource> getWarrantyExpiringSoon(int days) {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(days);
        return itResourceRepository.findWarrantyExpiringSoon(now, futureDate);
    }
    
    public List<String> getAllBrands() {
        return itResourceRepository.findAllBrands();
    }
    
    public List<String> getAllVendors() {
        return itResourceRepository.findAllVendors();
    }
    
    public List<String> getAllResourceLocations() {
        return itResourceRepository.findAllResourceLocations();
    }
    
    public Long getResourceCountByType(ITResource.ResourceType resourceType) {
        return itResourceRepository.countByResourceType(resourceType);
    }
    
    public Long getResourceCountByStatus(ITResource.ResourceStatus status) {
        return itResourceRepository.countByStatus(status);
    }
    
    public boolean existsByResourceCode(String resourceCode) {
        return itResourceRepository.findByResourceCode(resourceCode).isPresent();
    }
    
    private void validateResource(ITResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null");
        }
        
        if (resource.getResourceCode() == null || resource.getResourceCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Resource code is required");
        }
        
        if (resource.getResourceName() == null || resource.getResourceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Resource name is required");
        }
        
        // Check for duplicate resource code (excluding current resource in update scenario)
        Optional<ITResource> existingByCode = itResourceRepository.findByResourceCode(resource.getResourceCode());
        if (existingByCode.isPresent() && 
            !existingByCode.get().getId().equals(resource.getId())) {
            throw new IllegalArgumentException("Resource code already exists: " + resource.getResourceCode());
        }
    }
    
    public long getTotalResourceCount() {
        return itResourceRepository.count();
    }
    
    public void updateResourceStatus(Long resourceId, ITResource.ResourceStatus status) {
        Optional<ITResource> resourceOpt = getResourceById(resourceId);
        if (resourceOpt.isPresent()) {
            ITResource resource = resourceOpt.get();
            resource.setStatus(status);
            itResourceRepository.save(resource);
        } else {
            throw new IllegalArgumentException("Resource not found with ID: " + resourceId);
        }
    }
}