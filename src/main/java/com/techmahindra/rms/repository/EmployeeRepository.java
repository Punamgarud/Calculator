package com.techmahindra.rms.repository;

import com.techmahindra.rms.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmployeeId(String employeeId);
    
    Optional<Employee> findByEmail(String email);
    
    List<Employee> findByDepartment(String department);
    
    List<Employee> findByDesignation(String designation);
    
    List<Employee> findByStatus(Employee.EmployeeStatus status);
    
    List<Employee> findByManagerId(String managerId);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.designation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department")
    List<String> findAllDepartments();
    
    @Query("SELECT DISTINCT e.designation FROM Employee e ORDER BY e.designation")
    List<String> findAllDesignations();
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    long countByStatus(@Param("status") Employee.EmployeeStatus status);
    
    @Query("SELECT e.department, COUNT(e) FROM Employee e WHERE e.status = 'ACTIVE' GROUP BY e.department")
    List<Object[]> countEmployeesByDepartment();
    
    boolean existsByEmployeeId(String employeeId);
    
    boolean existsByEmail(String email);
}