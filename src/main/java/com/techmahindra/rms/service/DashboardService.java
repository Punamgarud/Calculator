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
    private ITResourceService resourceService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ResourceAllocationService allocationService;
    
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Employee Statistics
        dashboardData.put("totalEmployees", employeeService.findAll().size());
        dashboardData.put("activeEmployees", employeeService.countByStatus(Employee.EmployeeStatus.ACTIVE));
        dashboardData.put("inactiveEmployees", employeeService.countByStatus(Employee.EmployeeStatus.INACTIVE));
        dashboardData.put("employeesByDepartment", employeeService.getEmployeeCountByDepartment());
        
        // Resource Statistics
        dashboardData.put("totalResources", resourceService.findAll().size());
        dashboardData.put("availableResources", resourceService.countByStatus(ITResource.ResourceStatus.AVAILABLE));
        dashboardData.put("allocatedResources", resourceService.countByStatus(ITResource.ResourceStatus.ALLOCATED));
        dashboardData.put("maintenanceResources", resourceService.countByStatus(ITResource.ResourceStatus.MAINTENANCE));
        dashboardData.put("retiredResources", resourceService.countByStatus(ITResource.ResourceStatus.RETIRED));
        dashboardData.put("resourcesByCategory", resourceService.getResourceCountByCategory());
        
        // Project Statistics
        dashboardData.put("totalProjects", projectService.findAll().size());
        dashboardData.put("activeProjects", projectService.countByStatus(Project.ProjectStatus.ACTIVE));
        dashboardData.put("completedProjects", projectService.countByStatus(Project.ProjectStatus.COMPLETED));
        dashboardData.put("onHoldProjects", projectService.countByStatus(Project.ProjectStatus.ON_HOLD));
        dashboardData.put("projectsByStatus", projectService.getProjectCountByStatus());
        
        // Allocation Statistics
        dashboardData.put("totalAllocations", allocationService.findAll().size());
        dashboardData.put("activeAllocations", allocationService.countByStatus(ResourceAllocation.AllocationStatus.ACTIVE));
        dashboardData.put("overdueAllocations", allocationService.getOverdueAllocations().size());
        dashboardData.put("allocationsByType", allocationService.getActiveAllocationCountByType());
        dashboardData.put("allocationsByDepartment", allocationService.getActiveAllocationCountByDepartment());
        
        // Recent Activity
        dashboardData.put("recentEmployees", employeeService.findAll().stream()
                .sorted((e1, e2) -> e2.getCreatedAt().compareTo(e1.getCreatedAt()))
                .limit(5)
                .toList());
        
        dashboardData.put("recentResources", resourceService.findAll().stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(5)
                .toList());
        
        dashboardData.put("recentProjects", projectService.findAll().stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(5)
                .toList());
        
        // Alerts and Notifications
        dashboardData.put("warrantyExpiringResources", resourceService.getResourcesWithWarrantyExpiring(30));
        dashboardData.put("projectsEndingSoon", projectService.getProjectsEndingSoon(30));
        dashboardData.put("allocationsReturningDue", allocationService.getAllocationsReturningInNextDays(7));
        
        return dashboardData;
    }
    
    public Map<String, Object> getEmployeeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", employeeService.findAll().size());
        stats.put("active", employeeService.countByStatus(Employee.EmployeeStatus.ACTIVE));
        stats.put("inactive", employeeService.countByStatus(Employee.EmployeeStatus.INACTIVE));
        stats.put("terminated", employeeService.countByStatus(Employee.EmployeeStatus.TERMINATED));
        stats.put("byDepartment", employeeService.getEmployeeCountByDepartment());
        stats.put("departments", employeeService.getAllDepartments());
        stats.put("designations", employeeService.getAllDesignations());
        
        return stats;
    }
    
    public Map<String, Object> getResourceStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", resourceService.findAll().size());
        stats.put("available", resourceService.countByStatus(ITResource.ResourceStatus.AVAILABLE));
        stats.put("allocated", resourceService.countByStatus(ITResource.ResourceStatus.ALLOCATED));
        stats.put("maintenance", resourceService.countByStatus(ITResource.ResourceStatus.MAINTENANCE));
        stats.put("retired", resourceService.countByStatus(ITResource.ResourceStatus.RETIRED));
        stats.put("lost", resourceService.countByStatus(ITResource.ResourceStatus.LOST));
        stats.put("byCategory", resourceService.getResourceCountByCategory());
        stats.put("brands", resourceService.getAllBrands());
        stats.put("locations", resourceService.getAllLocations());
        
        return stats;
    }
    
    public Map<String, Object> getProjectStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", projectService.findAll().size());
        stats.put("planning", projectService.countByStatus(Project.ProjectStatus.PLANNING));
        stats.put("active", projectService.countByStatus(Project.ProjectStatus.ACTIVE));
        stats.put("onHold", projectService.countByStatus(Project.ProjectStatus.ON_HOLD));
        stats.put("completed", projectService.countByStatus(Project.ProjectStatus.COMPLETED));
        stats.put("cancelled", projectService.countByStatus(Project.ProjectStatus.CANCELLED));
        stats.put("byStatus", projectService.getProjectCountByStatus());
        stats.put("clients", projectService.getAllClients());
        stats.put("projectManagers", projectService.getAllProjectManagers());
        
        return stats;
    }
    
    public Map<String, Object> getAllocationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", allocationService.findAll().size());
        stats.put("active", allocationService.countByStatus(ResourceAllocation.AllocationStatus.ACTIVE));
        stats.put("returned", allocationService.countByStatus(ResourceAllocation.AllocationStatus.RETURNED));
        stats.put("overdue", allocationService.countByStatus(ResourceAllocation.AllocationStatus.OVERDUE));
        stats.put("lost", allocationService.countByStatus(ResourceAllocation.AllocationStatus.LOST));
        stats.put("cancelled", allocationService.countByStatus(ResourceAllocation.AllocationStatus.CANCELLED));
        stats.put("byType", allocationService.getActiveAllocationCountByType());
        stats.put("byDepartment", allocationService.getActiveAllocationCountByDepartment());
        
        return stats;
    }
    
    public List<ResourceAllocation> getOverdueAllocations() {
        return allocationService.getOverdueAllocations();
    }
    
    public List<ITResource> getWarrantyExpiringResources(int days) {
        return resourceService.getResourcesWithWarrantyExpiring(days);
    }
    
    public List<Project> getProjectsEndingSoon(int days) {
        return projectService.getProjectsEndingSoon(days);
    }
    
    public List<ResourceAllocation> getAllocationsReturningDue(int days) {
        return allocationService.getAllocationsReturningInNextDays(days);
    }
    
    public Map<String, Object> getMonthlyReport(int year, int month) {
        Map<String, Object> report = new HashMap<>();
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        List<ResourceAllocation> monthlyAllocations = allocationService.getAllocationsBetweenDates(startDate, endDate);
        
        report.put("period", startDate.getMonth().name() + " " + year);
        report.put("newAllocations", monthlyAllocations.size());
        report.put("allocations", monthlyAllocations);
        
        return report;
    }
}