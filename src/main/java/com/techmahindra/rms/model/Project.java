package com.techmahindra.rms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Project code is required")
    @Column(name = "project_code", unique = true, nullable = false)
    private String projectCode;
    
    @NotBlank(message = "Project name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Client name is required")
    @Column(name = "client_name", nullable = false)
    private String clientName;
    
    @Column(name = "project_manager")
    private String projectManager;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status = ProjectStatus.PLANNING;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ProjectPriority priority = ProjectPriority.MEDIUM;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "technology_stack", columnDefinition = "TEXT")
    private String technologyStack;
    
    @Column(name = "team_size")
    private Integer teamSize;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ResourceAllocation> resourceAllocations;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Project() {}
    
    public Project(String projectCode, String name, String clientName, String projectManager) {
        this.projectCode = projectCode;
        this.name = name;
        this.clientName = clientName;
        this.projectManager = projectManager;
        this.status = ProjectStatus.PLANNING;
        this.priority = ProjectPriority.MEDIUM;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProjectCode() {
        return projectCode;
    }
    
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public BigDecimal getBudget() {
        return budget;
    }
    
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
    
    public ProjectStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
    
    public ProjectPriority getPriority() {
        return priority;
    }
    
    public void setPriority(ProjectPriority priority) {
        this.priority = priority;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
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
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<ResourceAllocation> getResourceAllocations() {
        return resourceAllocations;
    }
    
    public void setResourceAllocations(List<ResourceAllocation> resourceAllocations) {
        this.resourceAllocations = resourceAllocations;
    }
    
    public boolean isActive() {
        return status == ProjectStatus.ACTIVE;
    }
    
    public boolean isCompleted() {
        return status == ProjectStatus.COMPLETED;
    }
    
    public enum ProjectStatus {
        PLANNING("Planning"),
        ACTIVE("Active"),
        ON_HOLD("On Hold"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");
        
        private final String displayName;
        
        ProjectStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum ProjectPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical");
        
        private final String displayName;
        
        ProjectPriority(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}