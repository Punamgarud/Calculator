package com.techmahindra.rms.service;

import com.techmahindra.rms.model.Project;
import com.techmahindra.rms.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    
    public Page<Project> getAllProjectsPageable(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }
    
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }
    
    public Optional<Project> getProjectByCode(String projectCode) {
        return projectRepository.findByProjectCode(projectCode);
    }
    
    public Project saveProject(Project project) {
        validateProject(project);
        return projectRepository.save(project);
    }
    
    public Project updateProject(Project project) {
        if (project.getId() == null) {
            throw new IllegalArgumentException("Project ID cannot be null for update operation");
        }
        validateProject(project);
        return projectRepository.save(project);
    }
    
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }
    
    public List<Project> getProjectsByStatus(Project.ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }
    
    public List<Project> getProjectsByPriority(Project.Priority priority) {
        return projectRepository.findByPriority(priority);
    }
    
    public List<Project> getProjectsByManager(String projectManager) {
        return projectRepository.findByProjectManager(projectManager);
    }
    
    public List<Project> getProjectsByClient(String clientName) {
        return projectRepository.findByClientName(clientName);
    }
    
    public List<Project> getProjectsByLocation(String location) {
        return projectRepository.findByLocation(location);
    }
    
    public Page<Project> searchProjects(String keyword, Pageable pageable) {
        return projectRepository.findByKeyword(keyword, pageable);
    }
    
    public List<Project> getActiveProjects() {
        return projectRepository.findActiveProjects();
    }
    
    public List<Project> getProjectsEndingBetween(LocalDate startDate, LocalDate endDate) {
        return projectRepository.findProjectsEndingBetween(startDate, endDate);
    }
    
    public List<Project> getProjectsStartingBetween(LocalDate startDate, LocalDate endDate) {
        return projectRepository.findProjectsStartingBetween(startDate, endDate);
    }
    
    public List<Project> getOverdueProjects() {
        return projectRepository.findOverdueProjects(LocalDate.now());
    }
    
    public List<Project> getProjectsEndingSoon(int days) {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(days);
        return projectRepository.findProjectsEndingSoon(now, futureDate);
    }
    
    public List<String> getAllClients() {
        return projectRepository.findAllClients();
    }
    
    public List<String> getAllProjectManagers() {
        return projectRepository.findAllProjectManagers();
    }
    
    public List<String> getAllProjectLocations() {
        return projectRepository.findAllProjectLocations();
    }
    
    public Long getProjectCountByStatus(Project.ProjectStatus status) {
        return projectRepository.countByStatus(status);
    }
    
    public Long getProjectCountByPriority(Project.Priority priority) {
        return projectRepository.countByPriority(priority);
    }
    
    public boolean existsByProjectCode(String projectCode) {
        return projectRepository.findByProjectCode(projectCode).isPresent();
    }
    
    private void validateProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        
        if (project.getProjectCode() == null || project.getProjectCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Project code is required");
        }
        
        if (project.getProjectName() == null || project.getProjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name is required");
        }
        
        if (project.getProjectManager() == null || project.getProjectManager().trim().isEmpty()) {
            throw new IllegalArgumentException("Project manager is required");
        }
        
        // Check for duplicate project code (excluding current project in update scenario)
        Optional<Project> existingByCode = projectRepository.findByProjectCode(project.getProjectCode());
        if (existingByCode.isPresent() && 
            !existingByCode.get().getId().equals(project.getId())) {
            throw new IllegalArgumentException("Project code already exists: " + project.getProjectCode());
        }
        
        // Validate dates
        if (project.getStartDate() != null && project.getEndDate() != null) {
            if (project.getStartDate().isAfter(project.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
        }
        
        if (project.getStartDate() != null && project.getPlannedEndDate() != null) {
            if (project.getStartDate().isAfter(project.getPlannedEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after planned end date");
            }
        }
    }
    
    public long getTotalProjectCount() {
        return projectRepository.count();
    }
    
    public void updateProjectStatus(Long projectId, Project.ProjectStatus status) {
        Optional<Project> projectOpt = getProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            project.setStatus(status);
            
            // Set end date when project is completed
            if (status == Project.ProjectStatus.COMPLETED && project.getEndDate() == null) {
                project.setEndDate(LocalDate.now());
            }
            
            projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Project not found with ID: " + projectId);
        }
    }
}