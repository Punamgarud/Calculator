package com.techmahindra.rms.controller;

import com.techmahindra.rms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> dashboardData = dashboardService.getDashboardData();
        
        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("employeeStatsByDepartment", dashboardService.getEmployeeStatsByDepartment());
        model.addAttribute("resourceStatsByType", dashboardService.getResourceStatsByType());
        model.addAttribute("projectStatsByStatus", dashboardService.getProjectStatsByStatus());
        model.addAttribute("allocationStatsByStatus", dashboardService.getAllocationStatsByStatus());
        model.addAttribute("criticalAlerts", dashboardService.getCriticalAlerts());
        model.addAttribute("projectAlerts", dashboardService.getProjectAlerts());
        model.addAttribute("resourceAlerts", dashboardService.getResourceAlerts());
        
        return "dashboard/index";
    }
    
    @GetMapping("/api/dashboard/data")
    @ResponseBody
    public Map<String, Object> getDashboardData() {
        return dashboardService.getDashboardData();
    }
    
    @GetMapping("/api/dashboard/employee-stats")
    @ResponseBody
    public Map<String, Long> getEmployeeStatsByDepartment() {
        return dashboardService.getEmployeeStatsByDepartment();
    }
    
    @GetMapping("/api/dashboard/resource-stats")
    @ResponseBody
    public Map<String, Long> getResourceStatsByType() {
        return dashboardService.getResourceStatsByType();
    }
    
    @GetMapping("/api/dashboard/project-stats")
    @ResponseBody
    public Map<String, Long> getProjectStatsByStatus() {
        return dashboardService.getProjectStatsByStatus();
    }
    
    @GetMapping("/api/dashboard/allocation-stats")
    @ResponseBody
    public Map<String, Long> getAllocationStatsByStatus() {
        return dashboardService.getAllocationStatsByStatus();
    }
}