package com.techmahindra.rms.controller;

import com.techmahindra.rms.model.Employee;
import com.techmahindra.rms.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping
    public String listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Employee> employees;
        if (search != null && !search.trim().isEmpty()) {
            employees = employeeService.searchEmployees(search, pageable);
            model.addAttribute("search", search);
        } else {
            employees = employeeService.findAll(pageable);
        }
        
        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employees.getTotalPages());
        model.addAttribute("totalItems", employees.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "employees/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("designations", employeeService.getAllDesignations());
        return "employees/form";
    }
    
    @PostMapping
    public String createEmployee(@Valid @ModelAttribute Employee employee, 
                               BindingResult result, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (!employeeService.isEmployeeIdUnique(employee.getEmployeeId())) {
            result.rejectValue("employeeId", "employee.employeeId.exists", "Employee ID already exists");
        }
        
        if (!employeeService.isEmailUnique(employee.getEmail())) {
            result.rejectValue("email", "employee.email.exists", "Email already exists");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            return "employees/form";
        }
        
        try {
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating employee: " + e.getMessage());
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            return "employees/form";
        }
    }
    
    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employees/view";
        }
        return "redirect:/employees";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            return "employees/form";
        }
        return "redirect:/employees";
    }
    
    @PostMapping("/{id}")
    public String updateEmployee(@PathVariable Long id,
                               @Valid @ModelAttribute Employee employee,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (!employeeService.isEmployeeIdUniqueForUpdate(employee.getEmployeeId(), id)) {
            result.rejectValue("employeeId", "employee.employeeId.exists", "Employee ID already exists");
        }
        
        if (!employeeService.isEmailUniqueForUpdate(employee.getEmail(), id)) {
            result.rejectValue("email", "employee.email.exists", "Email already exists");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            return "employees/form";
        }
        
        try {
            employeeService.update(id, employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating employee: " + e.getMessage());
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            return "employees/form";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }
    
    @PostMapping("/{id}/deactivate")
    public String deactivateEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deactivateEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deactivating employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }
    
    @PostMapping("/{id}/activate")
    public String activateEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.activateEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee activated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error activating employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }
    
    // API Endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<Employee>> getEmployeesApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Employee> employees;
        if (search != null && !search.trim().isEmpty()) {
            employees = employeeService.searchEmployees(search, pageable);
        } else {
            employees = employeeService.findAll(pageable);
        }
        
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Employee> getEmployeeApi(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/api/departments")
    @ResponseBody
    public ResponseEntity<List<String>> getDepartments() {
        return ResponseEntity.ok(employeeService.getAllDepartments());
    }
    
    @GetMapping("/api/designations")
    @ResponseBody
    public ResponseEntity<List<String>> getDesignations() {
        return ResponseEntity.ok(employeeService.getAllDesignations());
    }
}