package com.techmahindra.rms.service;

import com.techmahindra.rms.model.Employee;
import com.techmahindra.rms.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Page<Employee> getAllEmployeesPageable(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
    
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    
    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }
    
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    public Employee saveEmployee(Employee employee) {
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }
    
    public Employee updateEmployee(Employee employee) {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null for update operation");
        }
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }
    
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
    }
    
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }
    
    public List<Employee> getEmployeesByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.findByStatus(status);
    }
    
    public List<Employee> getEmployeesByDepartmentAndStatus(String department, Employee.EmployeeStatus status) {
        return employeeRepository.findByDepartmentAndStatus(department, status);
    }
    
    public List<Employee> getEmployeesByManagerId(String managerId) {
        return employeeRepository.findByManagerId(managerId);
    }
    
    public Page<Employee> searchEmployees(String keyword, Pageable pageable) {
        return employeeRepository.findByKeyword(keyword, pageable);
    }
    
    public List<Employee> getEmployeesByMinimumExperience(Integer minExperience) {
        return employeeRepository.findByMinimumExperience(minExperience);
    }
    
    public List<Employee> getEmployeesBySkill(String skill) {
        return employeeRepository.findBySkill(skill);
    }
    
    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }
    
    public List<String> getAllDesignations() {
        return employeeRepository.findAllDesignations();
    }
    
    public List<String> getAllLocations() {
        return employeeRepository.findAllLocations();
    }
    
    public Long getEmployeeCountByDepartment(String department) {
        return employeeRepository.countByDepartment(department);
    }
    
    public Long getEmployeeCountByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.countByStatus(status);
    }
    
    public boolean existsByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return employeeRepository.findByEmail(email).isPresent();
    }
    
    private void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        
        if (employee.getEmployeeId() == null || employee.getEmployeeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Check for duplicate employee ID (excluding current employee in update scenario)
        Optional<Employee> existingByEmployeeId = employeeRepository.findByEmployeeId(employee.getEmployeeId());
        if (existingByEmployeeId.isPresent() && 
            !existingByEmployeeId.get().getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Employee ID already exists: " + employee.getEmployeeId());
        }
        
        // Check for duplicate email (excluding current employee in update scenario)
        Optional<Employee> existingByEmail = employeeRepository.findByEmail(employee.getEmail());
        if (existingByEmail.isPresent() && 
            !existingByEmail.get().getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Email already exists: " + employee.getEmail());
        }
    }
    
    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }
    
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByStatus(Employee.EmployeeStatus.ACTIVE);
    }
}