package com.techmahindra.rms.service;

import com.techmahindra.rms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private ITResourceService itResourceService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ResourceAllocationService allocationService;
    
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Employee Statistics
        dashboardData.put("totalEmployees", employeeService.getTotalEmployeeCount());
        dashboardData.put("activeEmployees", employeeService.getEmployeeCountByStatus(Employee.EmployeeStatus.ACTIVE));
        dashboardData.put("inactiveEmployees", employeeService.getEmployeeCountByStatus(Employee.EmployeeStatus.INACTIVE));
        dashboardData.put("employeesOnLeave", employeeService.getEmployeeCountByStatus(Employee.EmployeeStatus.ON_LEAVE));
        
        // IT Resource Statistics
        dashboardData.put("totalResources", itResourceService.getTotalResourceCount());
        dashboardData.put("availableResources", itResourceService.getResourceCountByStatus(ITResource.ResourceStatus.AVAILABLE));
        dashboardData.put("allocatedResources", itResourceService.getResourceCountByStatus(ITResource.ResourceStatus.ALLOCATED));
        dashboardData.put("maintenanceResources", itResourceService.getResourceCountByStatus(ITResource.ResourceStatus.MAINTENANCE));
        dashboardData.put("damagedResources", itResourceService.getResourceCountByStatus(ITResource.ResourceStatus.DAMAGED));
        
        // Project Statistics
        dashboardData.put("totalProjects", projectService.getTotalProjectCount());
        dashboardData.put("activeProjects", projectService.getProjectCountByStatus(Project.ProjectStatus.ACTIVE));
        dashboardData.put("completedProjects", projectService.getProjectCountByStatus(Project.ProjectStatus.COMPLETED));
        dashboardData.put("onHoldProjects", projectService.getProjectCountByStatus(Project.ProjectStatus.ON_HOLD));
        dashboardData.put("planningProjects", projectService.getProjectCountByStatus(Project.ProjectStatus.PLANNING));
        
        // Priority-based Project Statistics
        dashboardData.put("highPriorityProjects", projectService.getProjectCountByPriority(Project.Priority.HIGH));
        dashboardData.put("criticalPriorityProjects", projectService.getProjectCountByPriority(Project.Priority.CRITICAL));
        
        // Resource Allocation Statistics
        dashboardData.put("totalAllocations", allocationService.getTotalAllocationCount());
        dashboardData.put("activeAllocations", allocationService.getAllocationCountByStatus(ResourceAllocation.AllocationStatus.ACTIVE));
        dashboardData.put("pendingApprovalAllocations", allocationService.getAllocationCountByStatus(ResourceAllocation.AllocationStatus.PENDING_APPROVAL));
        dashboardData.put("overdueAllocations", (long) allocationService.getOverdueAllocations().size());
        
        // Recent Activities
        dashboardData.put("recentAllocations", allocationService.getAllocationsByDateRange(LocalDate.now().minusDays(7), LocalDate.now()));
        dashboardData.put("allocationsEndingSoon", allocationService.getAllocationsEndingSoon(7));
        dashboardData.put("projectsEndingSoon", projectService.getProjectsEndingSoon(30));
        dashboardData.put("overdueProjects", projectService.getOverdueProjects());
        dashboardData.put("warrantyExpiringSoon", itResourceService.getWarrantyExpiringSoon(30));
        dashboardData.put("expiredWarrantyResources", itResourceService.getExpiredWarrantyResources());
        
        return dashboardData;
    }
    
    public Map<String, Long> getEmployeeStatsByDepartment() {
        Map<String, Long> stats = new HashMap<>();
        List<String> departments = employeeService.getAllDepartments();
        
        for (String department : departments) {
            stats.put(department, employeeService.getEmployeeCountByDepartment(department));
        }
        
        return stats;
    }
    
    public Map<String, Long> getResourceStatsByType() {
        Map<String, Long> stats = new HashMap<>();
        
        for (ITResource.ResourceType type : ITResource.ResourceType.values()) {
            stats.put(type.name(), itResourceService.getResourceCountByType(type));
        }
        
        return stats;
    }
    
    public Map<String, Long> getProjectStatsByStatus() {
        Map<String, Long> stats = new HashMap<>();
        
        for (Project.ProjectStatus status : Project.ProjectStatus.values()) {
            stats.put(status.name(), projectService.getProjectCountByStatus(status));
        }
        
        return stats;
    }
    
    public Map<String, Long> getAllocationStatsByStatus() {
        Map<String, Long> stats = new HashMap<>();
        
        for (ResourceAllocation.AllocationStatus status : ResourceAllocation.AllocationStatus.values()) {
            stats.put(status.name(), allocationService.getAllocationCountByStatus(status));
        }
        
        return stats;
    }
    
    public List<ResourceAllocation> getCriticalAlerts() {
        List<ResourceAllocation> overdueAllocations = allocationService.getOverdueAllocations();
        List<ResourceAllocation> endingSoon = allocationService.getAllocationsEndingSoon(3);
        
        overdueAllocations.addAll(endingSoon);
        return overdueAllocations;
    }
    
    public List<Project> getProjectAlerts() {
        List<Project> overdueProjects = projectService.getOverdueProjects();
        List<Project> endingSoon = projectService.getProjectsEndingSoon(7);
        
        overdueProjects.addAll(endingSoon);
        return overdueProjects;
    }
    
    public List<ITResource> getResourceAlerts() {
        List<ITResource> expiredWarranty = itResourceService.getExpiredWarrantyResources();
        List<ITResource> expiringSoon = itResourceService.getWarrantyExpiringSoon(30);
        
        expiredWarranty.addAll(expiringSoon);
        return expiredWarranty;
    }
}