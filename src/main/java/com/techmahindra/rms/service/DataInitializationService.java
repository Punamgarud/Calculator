package com.techmahindra.rms.service;

import com.techmahindra.rms.model.*;
import com.techmahindra.rms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ITResourceRepository resourceRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ResourceAllocationRepository allocationRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (employeeRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create sample employees
        List<Employee> employees = List.of(
            new Employee("TM001", "John", "Doe", "john.doe@techmahindra.com", "+91-9876543210", "IT", "Software Engineer"),
            new Employee("TM002", "Jane", "Smith", "jane.smith@techmahindra.com", "+91-9876543211", "IT", "Senior Software Engineer"),
            new Employee("TM003", "Mike", "Johnson", "mike.johnson@techmahindra.com", "+91-9876543212", "HR", "HR Manager"),
            new Employee("TM004", "Sarah", "Williams", "sarah.williams@techmahindra.com", "+91-9876543213", "Finance", "Financial Analyst"),
            new Employee("TM005", "David", "Brown", "david.brown@techmahindra.com", "+91-9876543214", "IT", "DevOps Engineer"),
            new Employee("TM006", "Lisa", "Davis", "lisa.davis@techmahindra.com", "+91-9876543215", "IT", "QA Engineer"),
            new Employee("TM007", "Robert", "Miller", "robert.miller@techmahindra.com", "+91-9876543216", "Operations", "Operations Manager"),
            new Employee("TM008", "Emily", "Wilson", "emily.wilson@techmahindra.com", "+91-9876543217", "IT", "System Administrator"),
            new Employee("TM009", "James", "Taylor", "james.taylor@techmahindra.com", "+91-9876543218", "Sales", "Sales Executive"),
            new Employee("TM010", "Anna", "Anderson", "anna.anderson@techmahindra.com", "+91-9876543219", "IT", "Technical Lead")
        );
        
        employees.forEach(emp -> {
            emp.setHireDate(LocalDate.now().minusDays((long) (Math.random() * 365)));
            emp.setManagerId(Math.random() > 0.5 ? "TM001" : "TM010");
        });
        
        employeeRepository.saveAll(employees);

        // Create sample IT resources
        List<ITResource> resources = List.of(
            createResource("LT001", "Dell Latitude 7420", ITResource.ResourceCategory.LAPTOP, "Dell", "Latitude 7420"),
            createResource("LT002", "HP EliteBook 840 G8", ITResource.ResourceCategory.LAPTOP, "HP", "EliteBook 840 G8"),
            createResource("LT003", "Lenovo ThinkPad T14", ITResource.ResourceCategory.LAPTOP, "Lenovo", "ThinkPad T14"),
            createResource("DT001", "Dell OptiPlex 7090", ITResource.ResourceCategory.DESKTOP, "Dell", "OptiPlex 7090"),
            createResource("DT002", "HP ProDesk 400 G7", ITResource.ResourceCategory.DESKTOP, "HP", "ProDesk 400 G7"),
            createResource("MN001", "Dell UltraSharp U2720Q", ITResource.ResourceCategory.MONITOR, "Dell", "UltraSharp U2720Q"),
            createResource("MN002", "LG 27UN850-W", ITResource.ResourceCategory.MONITOR, "LG", "27UN850-W"),
            createResource("MN003", "Samsung Odyssey G7", ITResource.ResourceCategory.MONITOR, "Samsung", "Odyssey G7"),
            createResource("PR001", "HP LaserJet Pro M404dn", ITResource.ResourceCategory.PRINTER, "HP", "LaserJet Pro M404dn"),
            createResource("MB001", "iPhone 13 Pro", ITResource.ResourceCategory.MOBILE, "Apple", "iPhone 13 Pro"),
            createResource("MB002", "Samsung Galaxy S22", ITResource.ResourceCategory.MOBILE, "Samsung", "Galaxy S22"),
            createResource("TB001", "iPad Pro 11", ITResource.ResourceCategory.TABLET, "Apple", "iPad Pro 11"),
            createResource("SW001", "Microsoft Office 365", ITResource.ResourceCategory.SOFTWARE, "Microsoft", "Office 365"),
            createResource("SW002", "Adobe Creative Suite", ITResource.ResourceCategory.SOFTWARE, "Adobe", "Creative Suite"),
            createResource("NW001", "Cisco Switch SG350-28", ITResource.ResourceCategory.NETWORK, "Cisco", "SG350-28")
        );
        
        resourceRepository.saveAll(resources);

        // Create sample projects
        List<Project> projects = List.of(
            createProject("PRJ001", "Digital Banking Platform", "HDFC Bank", "TM010"),
            createProject("PRJ002", "E-commerce Mobile App", "Reliance Retail", "TM001"),
            createProject("PRJ003", "Cloud Migration Project", "Tata Steel", "TM010"),
            createProject("PRJ004", "AI Chatbot Development", "Airtel", "TM002"),
            createProject("PRJ005", "IoT Dashboard", "Mahindra Auto", "TM001"),
            createProject("PRJ006", "Data Analytics Platform", "TCS Internal", "TM010"),
            createProject("PRJ007", "Mobile Banking App", "ICICI Bank", "TM002")
        );
        
        projectRepository.saveAll(projects);

        // Create sample allocations
        List<Employee> savedEmployees = employeeRepository.findAll();
        List<ITResource> savedResources = resourceRepository.findAll();
        List<Project> savedProjects = projectRepository.findAll();
        
        // Allocate laptops to employees
        for (int i = 0; i < Math.min(savedEmployees.size(), 8); i++) {
            if (i < savedResources.size()) {
                ITResource resource = savedResources.get(i);
                Employee employee = savedEmployees.get(i);
                Project project = savedProjects.get(i % savedProjects.size());
                
                ResourceAllocation allocation = new ResourceAllocation();
                allocation.setEmployee(employee);
                allocation.setResource(resource);
                allocation.setProject(project);
                allocation.setAllocationDate(LocalDate.now().minusDays((long) (Math.random() * 30)));
                allocation.setExpectedReturnDate(LocalDate.now().plusDays((long) (Math.random() * 180 + 30)));
                allocation.setAllocationType(ResourceAllocation.AllocationType.PROJECT_SPECIFIC);
                allocation.setPurpose("Development work for " + project.getName());
                allocation.setAllocatedBy("admin");
                allocation.setStatus(ResourceAllocation.AllocationStatus.ACTIVE);
                
                allocationRepository.save(allocation);
                
                // Update resource status
                resource.setStatus(ITResource.ResourceStatus.ALLOCATED);
                resourceRepository.save(resource);
            }
        }
        
        // Create one overdue allocation for demonstration
        if (!savedEmployees.isEmpty() && savedResources.size() > 8) {
            ITResource resource = savedResources.get(8);
            Employee employee = savedEmployees.get(0);
            
            ResourceAllocation overdueAllocation = new ResourceAllocation();
            overdueAllocation.setEmployee(employee);
            overdueAllocation.setResource(resource);
            overdueAllocation.setAllocationDate(LocalDate.now().minusDays(60));
            overdueAllocation.setExpectedReturnDate(LocalDate.now().minusDays(10)); // Overdue
            overdueAllocation.setAllocationType(ResourceAllocation.AllocationType.TEMPORARY);
            overdueAllocation.setPurpose("Testing purposes");
            overdueAllocation.setAllocatedBy("admin");
            overdueAllocation.setStatus(ResourceAllocation.AllocationStatus.ACTIVE);
            
            allocationRepository.save(overdueAllocation);
            
            resource.setStatus(ITResource.ResourceStatus.ALLOCATED);
            resourceRepository.save(resource);
        }
    }

    private ITResource createResource(String assetTag, String name, ITResource.ResourceCategory category, 
                                    String brand, String model) {
        ITResource resource = new ITResource();
        resource.setAssetTag(assetTag);
        resource.setName(name);
        resource.setCategory(category);
        resource.setBrand(brand);
        resource.setModel(model);
        resource.setSerialNumber("SN" + assetTag + System.currentTimeMillis() % 10000);
        resource.setPurchaseDate(LocalDate.now().minusDays((long) (Math.random() * 365)));
        resource.setPurchaseCost(new BigDecimal(50000 + (Math.random() * 100000)));
        resource.setWarrantyExpiry(LocalDate.now().plusDays((long) (Math.random() * 1095))); // 0-3 years
        resource.setStatus(ITResource.ResourceStatus.AVAILABLE);
        resource.setLocation("Pune Office");
        resource.setSpecifications(generateSpecifications(category));
        return resource;
    }
    
    private Project createProject(String projectCode, String name, String client, String manager) {
        Project project = new Project();
        project.setProjectCode(projectCode);
        project.setName(name);
        project.setClientName(client);
        project.setProjectManager(manager);
        project.setDescription("Strategic project for " + client + " focusing on digital transformation");
        project.setStartDate(LocalDate.now().minusDays((long) (Math.random() * 90)));
        project.setEndDate(LocalDate.now().plusDays((long) (Math.random() * 365 + 30)));
        project.setBudget(new BigDecimal(1000000 + (Math.random() * 5000000)));
        project.setStatus(Math.random() > 0.3 ? Project.ProjectStatus.ACTIVE : Project.ProjectStatus.PLANNING);
        project.setPriority(Project.ProjectPriority.values()[(int) (Math.random() * Project.ProjectPriority.values().length)]);
        project.setLocation("Pune");
        project.setTechnologyStack("Java, Spring Boot, React, MySQL, AWS");
        project.setTeamSize((int) (5 + Math.random() * 15));
        return project;
    }
    
    private String generateSpecifications(ITResource.ResourceCategory category) {
        return switch (category) {
            case LAPTOP -> "Intel i7-11th Gen, 16GB RAM, 512GB SSD, Windows 11 Pro";
            case DESKTOP -> "Intel i5-11th Gen, 8GB RAM, 256GB SSD, Windows 11 Pro";
            case MONITOR -> "27-inch, 4K UHD, USB-C, Height Adjustable";
            case MOBILE -> "128GB Storage, 5G Enabled, Dual Camera";
            case TABLET -> "11-inch, WiFi + Cellular, 256GB Storage";
            case PRINTER -> "Laser, Duplex Printing, Network Ready";
            case SOFTWARE -> "Cloud-based License, Multi-user";
            case NETWORK -> "24-port Gigabit Switch, Managed";
            default -> "Standard specifications";
        };
    }
}