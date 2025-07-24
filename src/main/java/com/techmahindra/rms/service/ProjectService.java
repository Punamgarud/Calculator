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
    
    public List<Project> findAll() {
        return projectRepository.findAll();
    }
    
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }
    
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }
    
    public Optional<Project> findByProjectCode(String projectCode) {
        return projectRepository.findByProjectCode(projectCode);
    }
    
    public List<Project> findByStatus(Project.ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }
    
    public List<Project> findByPriority(Project.ProjectPriority priority) {
        return projectRepository.findByPriority(priority);
    }
    
    public List<Project> findByClientName(String clientName) {
        return projectRepository.findByClientName(clientName);
    }
    
    public List<Project> findByProjectManager(String projectManager) {
        return projectRepository.findByProjectManager(projectManager);
    }
    
    public List<Project> findByLocation(String location) {
        return projectRepository.findByLocation(location);
    }
    
    public Page<Project> searchProjects(String keyword, Pageable pageable) {
        return projectRepository.findByKeyword(keyword, pageable);
    }
    
    public Project save(Project project) {
        validateProject(project);
        return projectRepository.save(project);
    }
    
    public Project update(Long id, Project projectDetails) {
        Optional<Project> existingProject = projectRepository.findById(id);
        if (existingProject.isPresent()) {
            Project project = existingProject.get();
            updateProjectFields(project, projectDetails);
            validateProject(project);
            return projectRepository.save(project);
        }
        throw new RuntimeException("Project not found with id: " + id);
    }
    
    public void deleteById(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new RuntimeException("Project not found with id: " + id);
        }
    }
    
    public void activateProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            Project proj = project.get();
            proj.setStatus(Project.ProjectStatus.ACTIVE);
            projectRepository.save(proj);
        } else {
            throw new RuntimeException("Project not found with id: " + id);
        }
    }
    
    public void completeProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            Project proj = project.get();
            proj.setStatus(Project.ProjectStatus.COMPLETED);
            projectRepository.save(proj);
        } else {
            throw new RuntimeException("Project not found with id: " + id);
        }
    }
    
    public void putProjectOnHold(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            Project proj = project.get();
            proj.setStatus(Project.ProjectStatus.ON_HOLD);
            projectRepository.save(proj);
        } else {
            throw new RuntimeException("Project not found with id: " + id);
        }
    }
    
    public void cancelProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            Project proj = project.get();
            proj.setStatus(Project.ProjectStatus.CANCELLED);
            projectRepository.save(proj);
        } else {
            throw new RuntimeException("Project not found with id: " + id);
        }
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
    
    public long countByStatus(Project.ProjectStatus status) {
        return projectRepository.countByStatus(status);
    }
    
    public List<Object[]> getProjectCountByStatus() {
        return projectRepository.countProjectsByStatus();
    }
    
    public List<Project> getProjectsEndingSoon(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        return projectRepository.findProjectsEndingSoon(startDate, endDate);
    }
    
    public List<Project> getActiveProjectsByPriority() {
        return projectRepository.findActiveProjectsByPriority();
    }
    
    public boolean isProjectCodeUnique(String projectCode) {
        return !projectRepository.existsByProjectCode(projectCode);
    }
    
    public boolean isProjectCodeUniqueForUpdate(String projectCode, Long currentProjectId) {
        Optional<Project> existingProject = projectRepository.findByProjectCode(projectCode);
        return !existingProject.isPresent() || existingProject.get().getId().equals(currentProjectId);
    }
    
    private void validateProject(Project project) {
        if (project.getProjectCode() == null || project.getProjectCode().trim().isEmpty()) {
            throw new RuntimeException("Project code is required");
        }
        
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new RuntimeException("Project name is required");
        }
        
        if (project.getClientName() == null || project.getClientName().trim().isEmpty()) {
            throw new RuntimeException("Client name is required");
        }
        
        if (project.getStartDate() != null && project.getEndDate() != null) {
            if (project.getStartDate().isAfter(project.getEndDate())) {
                throw new RuntimeException("End date must be after start date");
            }
        }
    }
    
    private void updateProjectFields(Project existing, Project updated) {
        existing.setProjectCode(updated.getProjectCode());
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setClientName(updated.getClientName());
        existing.setProjectManager(updated.getProjectManager());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setBudget(updated.getBudget());
        existing.setStatus(updated.getStatus());
        existing.setPriority(updated.getPriority());
        existing.setLocation(updated.getLocation());
        existing.setTechnologyStack(updated.getTechnologyStack());
        existing.setTeamSize(updated.getTeamSize());
        existing.setNotes(updated.getNotes());
    }
}