package com.techmahindra.rms.controller;

import com.techmahindra.rms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAllAttributes(dashboardData);
        return "dashboard/index";
    }
    
    @GetMapping("/api/dashboard/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = dashboardService.getDashboardData();
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/api/dashboard/employees")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEmployeeStatistics() {
        Map<String, Object> stats = dashboardService.getEmployeeStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/api/dashboard/resources")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getResourceStatistics() {
        Map<String, Object> stats = dashboardService.getResourceStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/api/dashboard/projects")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProjectStatistics() {
        Map<String, Object> stats = dashboardService.getProjectStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/api/dashboard/allocations")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllocationStatistics() {
        Map<String, Object> stats = dashboardService.getAllocationStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/api/dashboard/overdue")
    @ResponseBody
    public ResponseEntity<?> getOverdueAllocations() {
        return ResponseEntity.ok(dashboardService.getOverdueAllocations());
    }
    
    @GetMapping("/api/dashboard/warranty-expiring")
    @ResponseBody
    public ResponseEntity<?> getWarrantyExpiringResources(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getWarrantyExpiringResources(days));
    }
    
    @GetMapping("/api/dashboard/projects-ending")
    @ResponseBody
    public ResponseEntity<?> getProjectsEndingSoon(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getProjectsEndingSoon(days));
    }
    
    @GetMapping("/api/dashboard/allocations-due")
    @ResponseBody
    public ResponseEntity<?> getAllocationsReturningDue(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(dashboardService.getAllocationsReturningDue(days));
    }
    
    @GetMapping("/api/dashboard/monthly-report")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMonthlyReport(
            @RequestParam int year, 
            @RequestParam int month) {
        Map<String, Object> report = dashboardService.getMonthlyReport(year, month);
        return ResponseEntity.ok(report);
    }
}