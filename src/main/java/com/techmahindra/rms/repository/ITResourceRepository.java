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
    
    Optional<ITResource> findByAssetTag(String assetTag);
    
    List<ITResource> findByCategory(ITResource.ResourceCategory category);
    
    List<ITResource> findByStatus(ITResource.ResourceStatus status);
    
    List<ITResource> findByBrand(String brand);
    
    List<ITResource> findByLocation(String location);
    
    @Query("SELECT r FROM ITResource r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.assetTag) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.serialNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ITResource> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT DISTINCT r.brand FROM ITResource r WHERE r.brand IS NOT NULL ORDER BY r.brand")
    List<String> findAllBrands();
    
    @Query("SELECT DISTINCT r.location FROM ITResource r WHERE r.location IS NOT NULL ORDER BY r.location")
    List<String> findAllLocations();
    
    @Query("SELECT COUNT(r) FROM ITResource r WHERE r.status = :status")
    long countByStatus(@Param("status") ITResource.ResourceStatus status);
    
    @Query("SELECT r.category, COUNT(r) FROM ITResource r GROUP BY r.category")
    List<Object[]> countResourcesByCategory();
    
    @Query("SELECT r FROM ITResource r WHERE r.warrantyExpiry BETWEEN :startDate AND :endDate")
    List<ITResource> findResourcesWithWarrantyExpiring(@Param("startDate") LocalDate startDate, 
                                                      @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM ITResource r WHERE r.status = 'AVAILABLE' AND r.category = :category")
    List<ITResource> findAvailableResourcesByCategory(@Param("category") ITResource.ResourceCategory category);
    
    boolean existsByAssetTag(String assetTag);
    
    boolean existsBySerialNumber(String serialNumber);
}