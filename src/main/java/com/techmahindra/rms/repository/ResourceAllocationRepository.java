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
    
    List<ResourceAllocation> findByProject(Project project);
    
    List<ResourceAllocation> findByItResource(ITResource itResource);
    
    List<ResourceAllocation> findByStatus(ResourceAllocation.AllocationStatus status);
    
    List<ResourceAllocation> findByType(ResourceAllocation.AllocationType type);
    
    List<ResourceAllocation> findByEmployeeAndStatus(Employee employee, 
                                                   ResourceAllocation.AllocationStatus status);
    
    List<ResourceAllocation> findByProjectAndStatus(Project project, 
                                                   ResourceAllocation.AllocationStatus status);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.employee.employeeId = :employeeId")
    List<ResourceAllocation> findByEmployeeId(@Param("employeeId") String employeeId);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.project.projectCode = :projectCode")
    List<ResourceAllocation> findByProjectCode(@Param("projectCode") String projectCode);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.itResource.resourceCode = :resourceCode")
    List<ResourceAllocation> findByResourceCode(@Param("resourceCode") String resourceCode);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.allocationDate BETWEEN :startDate AND :endDate")
    List<ResourceAllocation> findByAllocationDateBetween(@Param("startDate") LocalDate startDate, 
                                                        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.expectedReturnDate BETWEEN :startDate AND :endDate")
    List<ResourceAllocation> findByExpectedReturnDateBetween(@Param("startDate") LocalDate startDate, 
                                                            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.expectedReturnDate < :currentDate AND ra.actualReturnDate IS NULL")
    List<ResourceAllocation> findOverdueAllocations(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.status = 'ACTIVE'")
    List<ResourceAllocation> findActiveAllocations();
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.expectedReturnDate BETWEEN :now AND :futureDate AND ra.status = 'ACTIVE'")
    List<ResourceAllocation> findAllocationsEndingSoon(@Param("now") LocalDate now, 
                                                      @Param("futureDate") LocalDate futureDate);
    
    @Query("SELECT COUNT(ra) FROM ResourceAllocation ra WHERE ra.employee = :employee AND ra.status = 'ACTIVE'")
    Long countActiveAllocationsByEmployee(@Param("employee") Employee employee);
    
    @Query("SELECT COUNT(ra) FROM ResourceAllocation ra WHERE ra.project = :project AND ra.status = 'ACTIVE'")
    Long countActiveAllocationsByProject(@Param("project") Project project);
    
    @Query("SELECT COUNT(ra) FROM ResourceAllocation ra WHERE ra.itResource = :resource AND ra.status = 'ACTIVE'")
    Long countActiveAllocationsByResource(@Param("resource") ITResource resource);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE " +
           "ra.employee.firstName LIKE CONCAT('%', :keyword, '%') OR " +
           "ra.employee.lastName LIKE CONCAT('%', :keyword, '%') OR " +
           "ra.employee.employeeId LIKE CONCAT('%', :keyword, '%') OR " +
           "ra.project.projectName LIKE CONCAT('%', :keyword, '%') OR " +
           "ra.project.projectCode LIKE CONCAT('%', :keyword, '%') OR " +
           "ra.itResource.resourceName LIKE CONCAT('%', :keyword, '%') OR " +
           "ra.itResource.resourceCode LIKE CONCAT('%', :keyword, '%')")
    Page<ResourceAllocation> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(ra) FROM ResourceAllocation ra WHERE ra.status = :status")
    Long countByStatus(@Param("status") ResourceAllocation.AllocationStatus status);
    
    @Query("SELECT ra FROM ResourceAllocation ra WHERE ra.approvedBy = :approver")
    List<ResourceAllocation> findByApprover(@Param("approver") String approver);
}