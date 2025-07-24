package com.techmahindra.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {
    
    @NotBlank(message = "Project code is required")
    @Column(name = "project_code", unique = true, nullable = false)
    private String projectCode;
    
    @NotBlank(message = "Project name is required")
    @Column(name = "project_name", nullable = false)
    private String projectName;
    
    @Column(name = "description", length = 2000)
    private String description;
    
    @Column(name = "client_name")
    private String clientName;
    
    @NotBlank(message = "Project manager is required")
    @Column(name = "project_manager", nullable = false)
    private String projectManager;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "project_status", nullable = false)
    private ProjectStatus status = ProjectStatus.PLANNING;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.MEDIUM;
    
    @DecimalMin(value = "0.0", message = "Budget must be positive")
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;
    
    @Column(name = "technology_stack", length = 1000)
    private String technologyStack;
    
    @Column(name = "team_size")
    private Integer teamSize;
    
    @Column(name = "location")
    private String location;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResourceAllocation> resourceAllocations = new ArrayList<>();
    
    public enum ProjectStatus {
        PLANNING, ACTIVE, ON_HOLD, COMPLETED, CANCELLED, DELAYED
    }
    
    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    // Constructors
    public Project() {}
    
    public Project(String projectCode, String projectName, String projectManager) {
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.projectManager = projectManager;
    }
    
    // Getters and Setters
    public String getProjectCode() {
        return projectCode;
    }
    
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public String getProjectManager() {
        return projectManager;
    }
    
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public LocalDate getPlannedEndDate() {
        return plannedEndDate;
    }
    
    public void setPlannedEndDate(LocalDate plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }
    
    public ProjectStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public BigDecimal getBudget() {
        return budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
    
    public String getTechnologyStack() {
        return technologyStack;
    }
    
    public void setTechnologyStack(String technologyStack) {
        this.technologyStack = technologyStack;
    }
    
    public Integer getTeamSize() {
        return teamSize;
    }
    
    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public List<ResourceAllocation> getResourceAllocations() {
        return resourceAllocations;
    }
    
    public void setResourceAllocations(List<ResourceAllocation> resourceAllocations) {
        this.resourceAllocations = resourceAllocations;
    }
}