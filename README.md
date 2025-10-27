# Tech Mahindra Resource Management System

A comprehensive full-stack Resource Management System built with Spring Boot, Hibernate JPA, Thymeleaf, and MySQL for managing employees, IT resources, projects, and resource allocations.

## Features

### Core Functionality
- **Employee Management**: Complete CRUD operations for employee records with status tracking
- **IT Resource Management**: Manage various IT assets including laptops, desktops, monitors, mobile devices, and software licenses
- **Project Management**: Track projects with status, priority, budget, and timeline management
- **Resource Allocation**: Allocate resources to employees for specific projects with return tracking
- **Dashboard Analytics**: Comprehensive dashboard with real-time statistics and charts
- **Reporting**: Built-in reporting functionality with overdue tracking and alerts

### Technical Features
- **Responsive Design**: Bootstrap 5 with mobile-first approach
- **Search & Filtering**: Advanced search capabilities across all modules
- **Pagination**: Efficient data pagination for large datasets
- **Sorting**: Multi-column sorting with visual indicators
- **Validation**: Server-side and client-side validation
- **Security**: Spring Security with role-based access control
- **Charts**: Interactive charts using Chart.js
- **RESTful APIs**: Complete REST API endpoints for all entities

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.1, Hibernate JPA
- **Frontend**: Thymeleaf, Bootstrap 5.3.2, jQuery 3.7.1
- **Database**: MySQL 8.0
- **Security**: Spring Security
- **Charts**: Chart.js 4.4.0
- **Icons**: Font Awesome 6.4.0
- **Build Tool**: Maven
- **Development**: Spring Boot DevTools

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd resource-management-system
```

### 2. Database Setup
Create a MySQL database:
```sql
CREATE DATABASE resource_management_db;
```

### 3. Configure Database
Update `src/main/resources/application.yml` with your database credentials:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/resource_management_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### 5. Access the Application
- URL: http://localhost:8080
- Login Credentials:
  - **Admin**: username: `admin`, password: `admin123`
  - **User**: username: `user`, password: `user123`

## Application Structure

```
src/main/java/com/techmahindra/rms/
├── config/                     # Configuration classes
│   └── SecurityConfig.java     # Security configuration
├── controller/                 # REST controllers
│   ├── DashboardController.java
│   ├── EmployeeController.java
│   ├── ITResourceController.java
│   ├── ProjectController.java
│   └── ResourceAllocationController.java
├── model/                      # JPA entities
│   ├── Employee.java
│   ├── ITResource.java
│   ├── Project.java
│   └── ResourceAllocation.java
├── repository/                 # JPA repositories
│   ├── EmployeeRepository.java
│   ├── ITResourceRepository.java
│   ├── ProjectRepository.java
│   └── ResourceAllocationRepository.java
├── service/                    # Business logic services
│   ├── DashboardService.java
│   ├── DataInitializationService.java
│   ├── EmployeeService.java
│   ├── ITResourceService.java
│   ├── ProjectService.java
│   └── ResourceAllocationService.java
└── ResourceManagementSystemApplication.java

src/main/resources/
├── templates/                  # Thymeleaf templates
│   ├── dashboard/
│   ├── employees/
│   ├── layout/
│   ├── projects/
│   ├── resources/
│   └── allocations/
└── application.yml             # Application configuration
```

## Features Overview

### Dashboard
- Real-time statistics for employees, resources, projects, and allocations
- Interactive charts showing resource distribution and employee department breakdown
- Recent activity feed
- Alert notifications for overdue returns, warranty expiring items, and project deadlines
- Quick action buttons for common tasks

### Employee Management
- Add, edit, view, and delete employees
- Employee status management (Active/Inactive/Terminated)
- Department and designation tracking
- Manager assignment
- Search and filter capabilities
- Bulk operations

### IT Resource Management
- Comprehensive asset tracking with unique asset tags
- Support for multiple resource categories (Laptops, Desktops, Monitors, etc.)
- Purchase information and warranty tracking
- Location and specification management
- Status tracking (Available/Allocated/Maintenance/Retired)
- Advanced search and filtering

### Project Management
- Project lifecycle management
- Client and project manager tracking
- Budget and timeline management
- Priority and status management
- Technology stack documentation
- Team size tracking

### Resource Allocation
- Allocate resources to employees for specific projects
- Return date tracking with overdue notifications
- Allocation type management (Permanent/Temporary/Project-specific)
- Purpose and notes documentation
- Return processing with audit trail
- Overdue tracking and alerts

## API Endpoints

### Dashboard APIs
- `GET /api/dashboard/data` - Get dashboard statistics
- `GET /api/dashboard/employees` - Get employee statistics
- `GET /api/dashboard/resources` - Get resource statistics
- `GET /api/dashboard/projects` - Get project statistics
- `GET /api/dashboard/allocations` - Get allocation statistics

### Employee APIs
- `GET /employees/api` - List employees with pagination
- `GET /employees/api/{id}` - Get employee by ID
- `GET /employees/api/departments` - Get all departments
- `GET /employees/api/designations` - Get all designations

### Resource APIs
- `GET /resources/api` - List resources with pagination
- `GET /resources/api/{id}` - Get resource by ID
- `GET /resources/api/categories` - Get resource categories
- `GET /resources/api/available` - Get available resources

### Project APIs
- `GET /projects/api` - List projects with pagination
- `GET /projects/api/{id}` - Get project by ID
- `GET /projects/api/clients` - Get all clients
- `GET /projects/api/active` - Get active projects

### Allocation APIs
- `GET /allocations/api` - List allocations with pagination
- `GET /allocations/api/{id}` - Get allocation by ID
- `GET /allocations/api/overdue` - Get overdue allocations

## Sample Data

The application includes a data initialization service that creates sample data on first run:
- 10 sample employees across different departments
- 15 IT resources of various categories
- 7 sample projects with different clients
- Sample resource allocations including one overdue allocation

## Security

- Spring Security integration with role-based access control
- In-memory user authentication (can be extended to database)
- CSRF protection
- Session management
- Secure password encoding with BCrypt

## Development Features

- Spring Boot DevTools for hot reloading
- Comprehensive logging configuration
- Profile-based configuration support
- Database migration support with Hibernate

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is proprietary software developed for Tech Mahindra.

## Support

For support and questions, please contact the development team.

---

**Tech Mahindra Resource Management System** - Efficiently manage your IT resources and employee allocations with comprehensive tracking and reporting capabilities.