package com.techmahindra.rms.repository;

import com.techmahindra.rms.model.ResourceAllocation;
import com.techmahindra.rms.model.Employee;
import com.techmahindra.rms.model.ITResource;
import com.techmahindra.rms.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResourceAllocationRepository extends JpaRepository<ResourceAllocation, Long> {
    
    List<ResourceAllocation> findByEmployee(Employee employee);
    
    List<ResourceAllocation> findByResource(ITResource resource);
    
    List<ResourceAllocation> findByProject(Project project);
    
    List<ResourceAllocation> findByStatus(ResourceAllocation.AllocationStatus status);
    
    List<ResourceAllocation> findByAllocationType(ResourceAllocation.AllocationType allocationType);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.employee = :employee AND ra.status = 'ACTIVE'")
    List<ResourceAllocation> findActiveAllocationsByEmployee(@Param("employee") Employee employee);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.resource = :resource AND ra.status = 'ACTIVE'")
    List<ResourceAllocation> findActiveAllocationsByResource(@Param("resource") ITResource resource);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.project = :project AND ra.status = 'ACTIVE'")
    List<ResourceAllocation> findActiveAllocationsByProject(@Param("project") Project project);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
           "ra.expectedReturnDate < :currentDate AND " +
           "ra.actualReturnDate IS NULL AND " +
           "ra.status = 'ACTIVE'")
    List<ResourceAllocation> findOverdueAllocations(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
           "ra.expectedReturnDate BETWEEN :startDate AND :endDate AND " +
           "ra.actualReturnDate IS NULL AND " +
           "ra.status = 'ACTIVE'")
    List<ResourceAllocation> findAllocationsReturningBetween(@Param("startDate") LocalDate startDate, 
                                                           @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
           "LOWER(ra.employee.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ra.employee.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ra.employee.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "(ra.resource IS NOT NULL AND LOWER(ra.resource.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
           "(ra.resource IS NOT NULL AND LOWER(ra.resource.assetTag) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
           "(ra.project IS NOT NULL AND LOWER(ra.project.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
           "(ra.project IS NOT NULL AND LOWER(ra.project.projectCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ResourceAllocation> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(ra) FROM ResourceAllocation ra WHERE ra.status = :status")
    long countByStatus(@Param("status") ResourceAllocation.AllocationStatus status);
    
    @Query("SELECT ra.allocationType, COUNT(ra) FROM ResourceAllocation ra WHERE ra.status = 'ACTIVE' GROUP BY ra.allocationType")
    List<Object[]> countActiveAllocationsByType();
    
    @Query("SELECT e.department, COUNT(ra) FROM ResourceAllocation ra " +
           "JOIN ra.employee e WHERE ra.status = 'ACTIVE' GROUP BY e.department")
    List<Object[]> countActiveAllocationsByDepartment();
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
           "ra.allocationDate BETWEEN :startDate AND :endDate")
    List<ResourceAllocation> findAllocationsBetweenDates(@Param("startDate") LocalDate startDate, 
                                                        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(ra) FROM ResourceAllocation ra WHERE " +
           "ra.employee = :employee AND ra.resource = :resource AND ra.status = 'ACTIVE'")
    long countActiveAllocationsForEmployeeAndResource(@Param("employee") Employee employee, 
                                                     @Param("resource") ITResource resource);
}