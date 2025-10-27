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
    private ITResourceRepository resourceRepository;
    
    public List<ITResource> findAll() {
        return resourceRepository.findAll();
    }
    
    public Page<ITResource> findAll(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }
    
    public Optional<ITResource> findById(Long id) {
        return resourceRepository.findById(id);
    }
    
    public Optional<ITResource> findByAssetTag(String assetTag) {
        return resourceRepository.findByAssetTag(assetTag);
    }
    
    public List<ITResource> findByCategory(ITResource.ResourceCategory category) {
        return resourceRepository.findByCategory(category);
    }
    
    public List<ITResource> findByStatus(ITResource.ResourceStatus status) {
        return resourceRepository.findByStatus(status);
    }
    
    public List<ITResource> findByBrand(String brand) {
        return resourceRepository.findByBrand(brand);
    }
    
    public List<ITResource> findByLocation(String location) {
        return resourceRepository.findByLocation(location);
    }
    
    public Page<ITResource> searchResources(String keyword, Pageable pageable) {
        return resourceRepository.findByKeyword(keyword, pageable);
    }
    
    public ITResource save(ITResource resource) {
        validateResource(resource);
        return resourceRepository.save(resource);
    }
    
    public ITResource update(Long id, ITResource resourceDetails) {
        Optional<ITResource> existingResource = resourceRepository.findById(id);
        if (existingResource.isPresent()) {
            ITResource resource = existingResource.get();
            updateResourceFields(resource, resourceDetails);
            validateResource(resource);
            return resourceRepository.save(resource);
        }
        throw new RuntimeException("Resource not found with id: " + id);
    }
    
    public void deleteById(Long id) {
        if (resourceRepository.existsById(id)) {
            resourceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Resource not found with id: " + id);
        }
    }
    
    public void retireResource(Long id) {
        Optional<ITResource> resource = resourceRepository.findById(id);
        if (resource.isPresent()) {
            ITResource res = resource.get();
            res.setStatus(ITResource.ResourceStatus.RETIRED);
            resourceRepository.save(res);
        } else {
            throw new RuntimeException("Resource not found with id: " + id);
        }
    }
    
    public void makeResourceAvailable(Long id) {
        Optional<ITResource> resource = resourceRepository.findById(id);
        if (resource.isPresent()) {
            ITResource res = resource.get();
            res.setStatus(ITResource.ResourceStatus.AVAILABLE);
            resourceRepository.save(res);
        } else {
            throw new RuntimeException("Resource not found with id: " + id);
        }
    }
    
    public void markResourceForMaintenance(Long id) {
        Optional<ITResource> resource = resourceRepository.findById(id);
        if (resource.isPresent()) {
            ITResource res = resource.get();
            res.setStatus(ITResource.ResourceStatus.MAINTENANCE);
            resourceRepository.save(res);
        } else {
            throw new RuntimeException("Resource not found with id: " + id);
        }
    }
    
    public List<String> getAllBrands() {
        return resourceRepository.findAllBrands();
    }
    
    public List<String> getAllLocations() {
        return resourceRepository.findAllLocations();
    }
    
    public long countByStatus(ITResource.ResourceStatus status) {
        return resourceRepository.countByStatus(status);
    }
    
    public List<Object[]> getResourceCountByCategory() {
        return resourceRepository.countResourcesByCategory();
    }
    
    public List<ITResource> getResourcesWithWarrantyExpiring(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        return resourceRepository.findResourcesWithWarrantyExpiring(startDate, endDate);
    }
    
    public List<ITResource> getAvailableResourcesByCategory(ITResource.ResourceCategory category) {
        return resourceRepository.findAvailableResourcesByCategory(category);
    }
    
    public boolean isAssetTagUnique(String assetTag) {
        return !resourceRepository.existsByAssetTag(assetTag);
    }
    
    public boolean isSerialNumberUnique(String serialNumber) {
        return !resourceRepository.existsBySerialNumber(serialNumber);
    }
    
    public boolean isAssetTagUniqueForUpdate(String assetTag, Long currentResourceId) {
        Optional<ITResource> existingResource = resourceRepository.findByAssetTag(assetTag);
        return !existingResource.isPresent() || existingResource.get().getId().equals(currentResourceId);
    }
    
    private void validateResource(ITResource resource) {
        if (resource.getAssetTag() == null || resource.getAssetTag().trim().isEmpty()) {
            throw new RuntimeException("Asset tag is required");
        }
        
        if (resource.getName() == null || resource.getName().trim().isEmpty()) {
            throw new RuntimeException("Resource name is required");
        }
        
        if (resource.getCategory() == null) {
            throw new RuntimeException("Category is required");
        }
    }
    
    private void updateResourceFields(ITResource existing, ITResource updated) {
        existing.setAssetTag(updated.getAssetTag());
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setSerialNumber(updated.getSerialNumber());
        existing.setSpecifications(updated.getSpecifications());
        existing.setPurchaseDate(updated.getPurchaseDate());
        existing.setPurchaseCost(updated.getPurchaseCost());
        existing.setWarrantyExpiry(updated.getWarrantyExpiry());
        existing.setStatus(updated.getStatus());
        existing.setLocation(updated.getLocation());
        existing.setNotes(updated.getNotes());
    }
}