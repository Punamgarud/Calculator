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
    
    List<Employee> findByStatus(Employee.EmployeeStatus status);
    
    List<Employee> findByDepartmentAndStatus(String department, Employee.EmployeeStatus status);
    
    @Query("SELECT e FROM Employee e WHERE e.managerId = :managerId")
    List<Employee> findByManagerId(@Param("managerId") String managerId);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.designation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department = :department")
    Long countByDepartment(@Param("department") String department);
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    Long countByStatus(@Param("status") Employee.EmployeeStatus status);
    
    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department")
    List<String> findAllDepartments();
    
    @Query("SELECT DISTINCT e.designation FROM Employee e ORDER BY e.designation")
    List<String> findAllDesignations();
    
    @Query("SELECT DISTINCT e.location FROM Employee e WHERE e.location IS NOT NULL ORDER BY e.location")
    List<String> findAllLocations();
    
    @Query("SELECT e FROM Employee e WHERE e.experienceYears >= :minExperience")
    List<Employee> findByMinimumExperience(@Param("minExperience") Integer minExperience);
    
    @Query("SELECT e FROM Employee e WHERE LOWER(e.skillSet) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<Employee> findBySkill(@Param("skill") String skill);
}