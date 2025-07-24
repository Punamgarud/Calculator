package com.techmahindra.rms.controller;

import com.techmahindra.rms.model.Employee;
import com.techmahindra.rms.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Employee.EmployeeStatus status,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employees;
        
        if (search != null && !search.trim().isEmpty()) {
            employees = employeeService.searchEmployees(search.trim(), pageable);
            model.addAttribute("search", search);
        } else {
            employees = employeeService.getAllEmployeesPageable(pageable);
        }
        
        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employees.getTotalPages());
        model.addAttribute("totalElements", employees.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("size", size);
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("designations", employeeService.getAllDesignations());
        model.addAttribute("locations", employeeService.getAllLocations());
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedStatus", status);
        
        return "employees/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("designations", employeeService.getAllDesignations());
        model.addAttribute("locations", employeeService.getAllLocations());
        return "employees/form";
    }
    
    @PostMapping
    public String createEmployee(@Valid @ModelAttribute Employee employee, 
                               BindingResult result, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            model.addAttribute("locations", employeeService.getAllLocations());
            return "employees/form";
        }
        
        try {
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Employee " + employee.getFullName() + " created successfully!");
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            model.addAttribute("locations", employeeService.getAllLocations());
            return "employees/form";
        }
    }
    
    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employees/view";
        } else {
            return "redirect:/employees?error=Employee not found";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            model.addAttribute("locations", employeeService.getAllLocations());
            return "employees/form";
        } else {
            return "redirect:/employees?error=Employee not found";
        }
    }
    
    @PostMapping("/{id}")
    public String updateEmployee(@PathVariable Long id, 
                               @Valid @ModelAttribute Employee employee, 
                               BindingResult result, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            model.addAttribute("locations", employeeService.getAllLocations());
            return "employees/form";
        }
        
        try {
            employee.setId(id);
            employeeService.updateEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Employee " + employee.getFullName() + " updated successfully!");
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("designations", employeeService.getAllDesignations());
            model.addAttribute("locations", employeeService.getAllLocations());
            return "employees/form";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            if (employee.isPresent()) {
                employeeService.deleteEmployee(id);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Employee " + employee.get().getFullName() + " deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Employee not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error deleting employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }
    
    @GetMapping("/department/{department}")
    public String getEmployeesByDepartment(@PathVariable String department, Model model) {
        model.addAttribute("employees", employeeService.getEmployeesByDepartment(department));
        model.addAttribute("department", department);
        return "employees/by-department";
    }
    
    @PostMapping("/{id}/status")
    public String updateEmployeeStatus(@PathVariable Long id, 
                                     @RequestParam Employee.EmployeeStatus status,
                                     RedirectAttributes redirectAttributes) {
        try {
            Optional<Employee> employeeOpt = employeeService.getEmployeeById(id);
            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();
                employee.setStatus(status);
                employeeService.updateEmployee(employee);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Employee status updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Employee not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error updating employee status: " + e.getMessage());
        }
        return "redirect:/employees";
    }
}