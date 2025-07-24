package com.techmahindra.rms.controller;

import com.techmahindra.rms.model.Project;
import com.techmahindra.rms.service.ProjectService;
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
@RequestMapping("/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping
    public String listProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Project> projects;
        if (search != null && !search.trim().isEmpty()) {
            projects = projectService.searchProjects(search, pageable);
            model.addAttribute("search", search);
        } else {
            projects = projectService.findAll(pageable);
        }
        
        model.addAttribute("projects", projects);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", projects.getTotalPages());
        model.addAttribute("totalItems", projects.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "projects/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("statuses", Arrays.asList(Project.ProjectStatus.values()));
        model.addAttribute("priorities", Arrays.asList(Project.ProjectPriority.values()));
        model.addAttribute("clients", projectService.getAllClients());
        model.addAttribute("projectManagers", projectService.getAllProjectManagers());
        model.addAttribute("locations", projectService.getAllProjectLocations());
        return "projects/form";
    }
    
    @PostMapping
    public String createProject(@Valid @ModelAttribute Project project, 
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (!projectService.isProjectCodeUnique(project.getProjectCode())) {
            result.rejectValue("projectCode", "project.projectCode.exists", "Project code already exists");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("statuses", Arrays.asList(Project.ProjectStatus.values()));
            model.addAttribute("priorities", Arrays.asList(Project.ProjectPriority.values()));
            model.addAttribute("clients", projectService.getAllClients());
            model.addAttribute("projectManagers", projectService.getAllProjectManagers());
            model.addAttribute("locations", projectService.getAllProjectLocations());
            return "projects/form";
        }
        
        try {
            projectService.save(project);
            redirectAttributes.addFlashAttribute("successMessage", "Project created successfully!");
            return "redirect:/projects";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating project: " + e.getMessage());
            model.addAttribute("statuses", Arrays.asList(Project.ProjectStatus.values()));
            model.addAttribute("priorities", Arrays.asList(Project.ProjectPriority.values()));
            model.addAttribute("clients", projectService.getAllClients());
            model.addAttribute("projectManagers", projectService.getAllProjectManagers());
            model.addAttribute("locations", projectService.getAllProjectLocations());
            return "projects/form";
        }
    }
    
    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            return "projects/view";
        }
        return "redirect:/projects";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            model.addAttribute("statuses", Arrays.asList(Project.ProjectStatus.values()));
            model.addAttribute("priorities", Arrays.asList(Project.ProjectPriority.values()));
            model.addAttribute("clients", projectService.getAllClients());
            model.addAttribute("projectManagers", projectService.getAllProjectManagers());
            model.addAttribute("locations", projectService.getAllProjectLocations());
            return "projects/form";
        }
        return "redirect:/projects";
    }
    
    @PostMapping("/{id}")
    public String updateProject(@PathVariable Long id,
                              @Valid @ModelAttribute Project project,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (!projectService.isProjectCodeUniqueForUpdate(project.getProjectCode(), id)) {
            result.rejectValue("projectCode", "project.projectCode.exists", "Project code already exists");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("statuses", Arrays.asList(Project.ProjectStatus.values()));
            model.addAttribute("priorities", Arrays.asList(Project.ProjectPriority.values()));
            model.addAttribute("clients", projectService.getAllClients());
            model.addAttribute("projectManagers", projectService.getAllProjectManagers());
            model.addAttribute("locations", projectService.getAllProjectLocations());
            return "projects/form";
        }
        
        try {
            projectService.update(id, project);
            redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully!");
            return "redirect:/projects";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating project: " + e.getMessage());
            model.addAttribute("statuses", Arrays.asList(Project.ProjectStatus.values()));
            model.addAttribute("priorities", Arrays.asList(Project.ProjectPriority.values()));
            model.addAttribute("clients", projectService.getAllClients());
            model.addAttribute("projectManagers", projectService.getAllProjectManagers());
            model.addAttribute("locations", projectService.getAllProjectLocations());
            return "projects/form";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting project: " + e.getMessage());
        }
        return "redirect:/projects";
    }
    
    @PostMapping("/{id}/activate")
    public String activateProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.activateProject(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project activated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error activating project: " + e.getMessage());
        }
        return "redirect:/projects";
    }
    
    @PostMapping("/{id}/complete")
    public String completeProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.completeProject(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project completed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error completing project: " + e.getMessage());
        }
        return "redirect:/projects";
    }
    
    @PostMapping("/{id}/hold")
    public String putProjectOnHold(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.putProjectOnHold(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project put on hold successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error putting project on hold: " + e.getMessage());
        }
        return "redirect:/projects";
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.cancelProject(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error cancelling project: " + e.getMessage());
        }
        return "redirect:/projects";
    }
    
    // API Endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<Project>> getProjectsApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Project> projects;
        if (search != null && !search.trim().isEmpty()) {
            projects = projectService.searchProjects(search, pageable);
        } else {
            projects = projectService.findAll(pageable);
        }
        
        return ResponseEntity.ok(projects);
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Project> getProjectApi(@PathVariable Long id) {
        Optional<Project> project = projectService.findById(id);
        return project.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/api/clients")
    @ResponseBody
    public ResponseEntity<List<String>> getClients() {
        return ResponseEntity.ok(projectService.getAllClients());
    }
    
    @GetMapping("/api/managers")
    @ResponseBody
    public ResponseEntity<List<String>> getProjectManagers() {
        return ResponseEntity.ok(projectService.getAllProjectManagers());
    }
    
    @GetMapping("/api/active")
    @ResponseBody
    public ResponseEntity<List<Project>> getActiveProjects() {
        return ResponseEntity.ok(projectService.getActiveProjectsByPriority());
    }
}