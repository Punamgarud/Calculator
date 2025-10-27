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
    
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
    
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }
    
    public Optional<Employee> findByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }
    
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    public List<Employee> findByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }
    
    public List<Employee> findByDesignation(String designation) {
        return employeeRepository.findByDesignation(designation);
    }
    
    public List<Employee> findByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.findByStatus(status);
    }
    
    public List<Employee> findByManagerId(String managerId) {
        return employeeRepository.findByManagerId(managerId);
    }
    
    public Page<Employee> searchEmployees(String keyword, Pageable pageable) {
        return employeeRepository.findByKeyword(keyword, pageable);
    }
    
    public Employee save(Employee employee) {
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }
    
    public Employee update(Long id, Employee employeeDetails) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            updateEmployeeFields(employee, employeeDetails);
            validateEmployee(employee);
            return employeeRepository.save(employee);
        }
        throw new RuntimeException("Employee not found with id: " + id);
    }
    
    public void deleteById(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }
    
    public void deactivateEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee emp = employee.get();
            emp.setStatus(Employee.EmployeeStatus.INACTIVE);
            employeeRepository.save(emp);
        } else {
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }
    
    public void activateEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee emp = employee.get();
            emp.setStatus(Employee.EmployeeStatus.ACTIVE);
            employeeRepository.save(emp);
        } else {
            throw new RuntimeException("Employee not found with id: " + id);
        }
    }
    
    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }
    
    public List<String> getAllDesignations() {
        return employeeRepository.findAllDesignations();
    }
    
    public long countByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.countByStatus(status);
    }
    
    public List<Object[]> getEmployeeCountByDepartment() {
        return employeeRepository.countEmployeesByDepartment();
    }
    
    public boolean isEmployeeIdUnique(String employeeId) {
        return !employeeRepository.existsByEmployeeId(employeeId);
    }
    
    public boolean isEmailUnique(String email) {
        return !employeeRepository.existsByEmail(email);
    }
    
    public boolean isEmployeeIdUniqueForUpdate(String employeeId, Long currentEmployeeId) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeId(employeeId);
        return !existingEmployee.isPresent() || existingEmployee.get().getId().equals(currentEmployeeId);
    }
    
    public boolean isEmailUniqueForUpdate(String email, Long currentEmployeeId) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(email);
        return !existingEmployee.isPresent() || existingEmployee.get().getId().equals(currentEmployeeId);
    }
    
    private void validateEmployee(Employee employee) {
        if (employee.getEmployeeId() == null || employee.getEmployeeId().trim().isEmpty()) {
            throw new RuntimeException("Employee ID is required");
        }
        
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            throw new RuntimeException("First name is required");
        }
        
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            throw new RuntimeException("Last name is required");
        }
        
        if (employee.getDepartment() == null || employee.getDepartment().trim().isEmpty()) {
            throw new RuntimeException("Department is required");
        }
        
        if (employee.getDesignation() == null || employee.getDesignation().trim().isEmpty()) {
            throw new RuntimeException("Designation is required");
        }
    }
    
    private void updateEmployeeFields(Employee existing, Employee updated) {
        existing.setEmployeeId(updated.getEmployeeId());
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setDepartment(updated.getDepartment());
        existing.setDesignation(updated.getDesignation());
        existing.setHireDate(updated.getHireDate());
        existing.setManagerId(updated.getManagerId());
        existing.setStatus(updated.getStatus());
    }
}