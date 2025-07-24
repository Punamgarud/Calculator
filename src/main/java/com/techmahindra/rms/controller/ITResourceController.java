package com.techmahindra.rms.controller;

import com.techmahindra.rms.model.ITResource;
import com.techmahindra.rms.service.ITResourceService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/resources")
public class ITResourceController {
    
    @Autowired
    private ITResourceService resourceService;
    
    @GetMapping
    public String listResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ITResource> resources;
        if (search != null && !search.trim().isEmpty()) {
            resources = resourceService.searchResources(search, pageable);
            model.addAttribute("search", search);
        } else {
            resources = resourceService.findAll(pageable);
        }
        
        model.addAttribute("resources", resources);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resources.getTotalPages());
        model.addAttribute("totalItems", resources.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "resources/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("resource", new ITResource());
        model.addAttribute("categories", Arrays.asList(ITResource.ResourceCategory.values()));
        model.addAttribute("statuses", Arrays.asList(ITResource.ResourceStatus.values()));
        model.addAttribute("brands", resourceService.getAllBrands());
        model.addAttribute("locations", resourceService.getAllLocations());
        return "resources/form";
    }
    
    @PostMapping
    public String createResource(@Valid @ModelAttribute ITResource resource, 
                               BindingResult result, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (!resourceService.isAssetTagUnique(resource.getAssetTag())) {
            result.rejectValue("assetTag", "resource.assetTag.exists", "Asset tag already exists");
        }
        
        if (resource.getSerialNumber() != null && !resource.getSerialNumber().trim().isEmpty() &&
            !resourceService.isSerialNumberUnique(resource.getSerialNumber())) {
            result.rejectValue("serialNumber", "resource.serialNumber.exists", "Serial number already exists");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("categories", Arrays.asList(ITResource.ResourceCategory.values()));
            model.addAttribute("statuses", Arrays.asList(ITResource.ResourceStatus.values()));
            model.addAttribute("brands", resourceService.getAllBrands());
            model.addAttribute("locations", resourceService.getAllLocations());
            return "resources/form";
        }
        
        try {
            resourceService.save(resource);
            redirectAttributes.addFlashAttribute("successMessage", "Resource created successfully!");
            return "redirect:/resources";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating resource: " + e.getMessage());
            model.addAttribute("categories", Arrays.asList(ITResource.ResourceCategory.values()));
            model.addAttribute("statuses", Arrays.asList(ITResource.ResourceStatus.values()));
            model.addAttribute("brands", resourceService.getAllBrands());
            model.addAttribute("locations", resourceService.getAllLocations());
            return "resources/form";
        }
    }
    
    @GetMapping("/{id}")
    public String viewResource(@PathVariable Long id, Model model) {
        Optional<ITResource> resource = resourceService.findById(id);
        if (resource.isPresent()) {
            model.addAttribute("resource", resource.get());
            return "resources/view";
        }
        return "redirect:/resources";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<ITResource> resource = resourceService.findById(id);
        if (resource.isPresent()) {
            model.addAttribute("resource", resource.get());
            model.addAttribute("categories", Arrays.asList(ITResource.ResourceCategory.values()));
            model.addAttribute("statuses", Arrays.asList(ITResource.ResourceStatus.values()));
            model.addAttribute("brands", resourceService.getAllBrands());
            model.addAttribute("locations", resourceService.getAllLocations());
            return "resources/form";
        }
        return "redirect:/resources";
    }
    
    @PostMapping("/{id}")
    public String updateResource(@PathVariable Long id,
                               @Valid @ModelAttribute ITResource resource,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (!resourceService.isAssetTagUniqueForUpdate(resource.getAssetTag(), id)) {
            result.rejectValue("assetTag", "resource.assetTag.exists", "Asset tag already exists");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("categories", Arrays.asList(ITResource.ResourceCategory.values()));
            model.addAttribute("statuses", Arrays.asList(ITResource.ResourceStatus.values()));
            model.addAttribute("brands", resourceService.getAllBrands());
            model.addAttribute("locations", resourceService.getAllLocations());
            return "resources/form";
        }
        
        try {
            resourceService.update(id, resource);
            redirectAttributes.addFlashAttribute("successMessage", "Resource updated successfully!");
            return "redirect:/resources";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating resource: " + e.getMessage());
            model.addAttribute("categories", Arrays.asList(ITResource.ResourceCategory.values()));
            model.addAttribute("statuses", Arrays.asList(ITResource.ResourceStatus.values()));
            model.addAttribute("brands", resourceService.getAllBrands());
            model.addAttribute("locations", resourceService.getAllLocations());
            return "resources/form";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteResource(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            resourceService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Resource deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting resource: " + e.getMessage());
        }
        return "redirect:/resources";
    }
    
    @PostMapping("/{id}/retire")
    public String retireResource(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            resourceService.retireResource(id);
            redirectAttributes.addFlashAttribute("successMessage", "Resource retired successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error retiring resource: " + e.getMessage());
        }
        return "redirect:/resources";
    }
    
    @PostMapping("/{id}/make-available")
    public String makeResourceAvailable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            resourceService.makeResourceAvailable(id);
            redirectAttributes.addFlashAttribute("successMessage", "Resource made available successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error making resource available: " + e.getMessage());
        }
        return "redirect:/resources";
    }
    
    @PostMapping("/{id}/maintenance")
    public String markForMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            resourceService.markResourceForMaintenance(id);
            redirectAttributes.addFlashAttribute("successMessage", "Resource marked for maintenance successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error marking resource for maintenance: " + e.getMessage());
        }
        return "redirect:/resources";
    }
    
    // API Endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<ITResource>> getResourcesApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ITResource> resources;
        if (search != null && !search.trim().isEmpty()) {
            resources = resourceService.searchResources(search, pageable);
        } else {
            resources = resourceService.findAll(pageable);
        }
        
        return ResponseEntity.ok(resources);
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ITResource> getResourceApi(@PathVariable Long id) {
        Optional<ITResource> resource = resourceService.findById(id);
        return resource.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/api/categories")
    @ResponseBody
    public ResponseEntity<ITResource.ResourceCategory[]> getCategories() {
        return ResponseEntity.ok(ITResource.ResourceCategory.values());
    }
    
    @GetMapping("/api/brands")
    @ResponseBody
    public ResponseEntity<List<String>> getBrands() {
        return ResponseEntity.ok(resourceService.getAllBrands());
    }
    
    @GetMapping("/api/locations")
    @ResponseBody
    public ResponseEntity<List<String>> getLocations() {
        return ResponseEntity.ok(resourceService.getAllLocations());
    }
    
    @GetMapping("/api/available")
    @ResponseBody
    public ResponseEntity<List<ITResource>> getAvailableResources(
            @RequestParam(required = false) ITResource.ResourceCategory category) {
        if (category != null) {
            return ResponseEntity.ok(resourceService.getAvailableResourcesByCategory(category));
        }
        return ResponseEntity.ok(resourceService.findByStatus(ITResource.ResourceStatus.AVAILABLE));
    }
}