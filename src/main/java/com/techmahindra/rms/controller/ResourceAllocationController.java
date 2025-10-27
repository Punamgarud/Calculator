package com.techmahindra.rms.controller;

import com.techmahindra.rms.model.*;
import com.techmahindra.rms.service.*;
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

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/allocations")
public class ResourceAllocationController {
    
    @Autowired
    private ResourceAllocationService allocationService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private ITResourceService resourceService;
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping
    public String listAllocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ResourceAllocation> allocations;
        if (search != null && !search.trim().isEmpty()) {
            allocations = allocationService.searchAllocations(search, pageable);
            model.addAttribute("search", search);
        } else {
            allocations = allocationService.findAll(pageable);
        }
        
        model.addAttribute("allocations", allocations);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", allocations.getTotalPages());
        model.addAttribute("totalItems", allocations.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "allocations/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("allocation", new ResourceAllocation());
        model.addAttribute("employees", employeeService.findByStatus(Employee.EmployeeStatus.ACTIVE));
        model.addAttribute("resources", resourceService.findByStatus(ITResource.ResourceStatus.AVAILABLE));
        model.addAttribute("projects", projectService.findByStatus(Project.ProjectStatus.ACTIVE));
        model.addAttribute("allocationTypes", Arrays.asList(ResourceAllocation.AllocationType.values()));
        return "allocations/form";
    }
    
    @PostMapping
    public String createAllocation(@Valid @ModelAttribute ResourceAllocation allocation, 
                                 BindingResult result, 
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.findByStatus(Employee.EmployeeStatus.ACTIVE));
            model.addAttribute("resources", resourceService.findByStatus(ITResource.ResourceStatus.AVAILABLE));
            model.addAttribute("projects", projectService.findByStatus(Project.ProjectStatus.ACTIVE));
            model.addAttribute("allocationTypes", Arrays.asList(ResourceAllocation.AllocationType.values()));
            return "allocations/form";
        }
        
        try {
            allocationService.save(allocation);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation created successfully!");
            return "redirect:/allocations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating allocation: " + e.getMessage());
            model.addAttribute("employees", employeeService.findByStatus(Employee.EmployeeStatus.ACTIVE));
            model.addAttribute("resources", resourceService.findByStatus(ITResource.ResourceStatus.AVAILABLE));
            model.addAttribute("projects", projectService.findByStatus(Project.ProjectStatus.ACTIVE));
            model.addAttribute("allocationTypes", Arrays.asList(ResourceAllocation.AllocationType.values()));
            return "allocations/form";
        }
    }
    
    @GetMapping("/{id}")
    public String viewAllocation(@PathVariable Long id, Model model) {
        Optional<ResourceAllocation> allocation = allocationService.findById(id);
        if (allocation.isPresent()) {
            model.addAttribute("allocation", allocation.get());
            return "allocations/view";
        }
        return "redirect:/allocations";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<ResourceAllocation> allocation = allocationService.findById(id);
        if (allocation.isPresent()) {
            model.addAttribute("allocation", allocation.get());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("resources", resourceService.findAll());
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("allocationTypes", Arrays.asList(ResourceAllocation.AllocationType.values()));
            model.addAttribute("allocationStatuses", Arrays.asList(ResourceAllocation.AllocationStatus.values()));
            return "allocations/form";
        }
        return "redirect:/allocations";
    }
    
    @PostMapping("/{id}")
    public String updateAllocation(@PathVariable Long id,
                                 @Valid @ModelAttribute ResourceAllocation allocation,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("resources", resourceService.findAll());
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("allocationTypes", Arrays.asList(ResourceAllocation.AllocationType.values()));
            model.addAttribute("allocationStatuses", Arrays.asList(ResourceAllocation.AllocationStatus.values()));
            return "allocations/form";
        }
        
        try {
            allocationService.update(id, allocation);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation updated successfully!");
            return "redirect:/allocations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating allocation: " + e.getMessage());
            model.addAttribute("employees", employeeService.findAll());
            model.addAttribute("resources", resourceService.findAll());
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("allocationTypes", Arrays.asList(ResourceAllocation.AllocationType.values()));
            model.addAttribute("allocationStatuses", Arrays.asList(ResourceAllocation.AllocationStatus.values()));
            return "allocations/form";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteAllocation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            allocationService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting allocation: " + e.getMessage());
        }
        return "redirect:/allocations";
    }
    
    @PostMapping("/{id}/return")
    public String returnResource(@PathVariable Long id, 
                               @RequestParam String returnedBy,
                               RedirectAttributes redirectAttributes) {
        try {
            allocationService.returnResource(id, returnedBy);
            redirectAttributes.addFlashAttribute("successMessage", "Resource returned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error returning resource: " + e.getMessage());
        }
        return "redirect:/allocations";
    }
    
    @PostMapping("/{id}/mark-lost")
    public String markAsLost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            allocationService.markAsLost(id);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation marked as lost successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error marking allocation as lost: " + e.getMessage());
        }
        return "redirect:/allocations";
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelAllocation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            allocationService.cancelAllocation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error cancelling allocation: " + e.getMessage());
        }
        return "redirect:/allocations";
    }
    
    @GetMapping("/overdue")
    public String listOverdueAllocations(Model model) {
        model.addAttribute("allocations", allocationService.getOverdueAllocations());
        return "allocations/overdue";
    }
    
    // API Endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<ResourceAllocation>> getAllocationsApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ResourceAllocation> allocations;
        if (search != null && !search.trim().isEmpty()) {
            allocations = allocationService.searchAllocations(search, pageable);
        } else {
            allocations = allocationService.findAll(pageable);
        }
        
        return ResponseEntity.ok(allocations);
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ResourceAllocation> getAllocationApi(@PathVariable Long id) {
        Optional<ResourceAllocation> allocation = allocationService.findById(id);
        return allocation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/api/overdue")
    @ResponseBody
    public ResponseEntity<?> getOverdueAllocationsApi() {
        return ResponseEntity.ok(allocationService.getOverdueAllocations());
    }
    
    @GetMapping("/api/returning-due")
    @ResponseBody
    public ResponseEntity<?> getAllocationsReturningDueApi(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(allocationService.getAllocationsReturningInNextDays(days));
    }
}