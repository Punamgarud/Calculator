package com.techmahindra.rms.repository;

import com.techmahindra.rms.model.ITResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITResourceRepository extends JpaRepository<ITResource, Long> {
    
    Optional<ITResource> findByResourceCode(String resourceCode);
    
    List<ITResource> findByResourceType(ITResource.ResourceType resourceType);
    
    List<ITResource> findByCategory(ITResource.ResourceCategory category);
    
    List<ITResource> findByStatus(ITResource.ResourceStatus status);
    
    List<ITResource> findByResourceTypeAndStatus(ITResource.ResourceType resourceType, 
                                                ITResource.ResourceStatus status);
    
    List<ITResource> findByCategoryAndStatus(ITResource.ResourceCategory category, 
                                           ITResource.ResourceStatus status);
    
    List<ITResource> findByBrand(String brand);
    
    List<ITResource> findByVendor(String vendor);
    
    List<ITResource> findByLocation(String location);
    
    @Query("SELECT r FROM ITResource r WHERE " +
           "LOWER(r.resourceName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.resourceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.serialNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ITResource> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(r) FROM ITResource r WHERE r.resourceType = :resourceType")
    Long countByResourceType(@Param("resourceType") ITResource.ResourceType resourceType);
    
    @Query("SELECT COUNT(r) FROM ITResource r WHERE r.status = :status")
    Long countByStatus(@Param("status") ITResource.ResourceStatus status);
    
    @Query("SELECT r FROM ITResource r WHERE r.status = 'AVAILABLE'")
    List<ITResource> findAvailableResources();
    
    @Query("SELECT r FROM ITResource r WHERE r.status = 'ALLOCATED'")
    List<ITResource> findAllocatedResources();
    
    @Query("SELECT r FROM ITResource r WHERE r.warrantyExpiryDate BETWEEN :startDate AND :endDate")
    List<ITResource> findByWarrantyExpiryDateBetween(@Param("startDate") LocalDate startDate, 
                                                    @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM ITResource r WHERE r.warrantyExpiryDate <= :date")
    List<ITResource> findExpiredWarrantyResources(@Param("date") LocalDate date);
    
    @Query("SELECT r FROM ITResource r WHERE r.warrantyExpiryDate BETWEEN :now AND :futureDate")
    List<ITResource> findWarrantyExpiringSoon(@Param("now") LocalDate now, 
                                             @Param("futureDate") LocalDate futureDate);
    
    @Query("SELECT DISTINCT r.brand FROM ITResource r WHERE r.brand IS NOT NULL ORDER BY r.brand")
    List<String> findAllBrands();
    
    @Query("SELECT DISTINCT r.vendor FROM ITResource r WHERE r.vendor IS NOT NULL ORDER BY r.vendor")
    List<String> findAllVendors();
    
    @Query("SELECT DISTINCT r.location FROM ITResource r WHERE r.location IS NOT NULL ORDER BY r.location")
    List<String> findAllResourceLocations();
}