# Tech Mahindra Resource Management System

A comprehensive full-stack Resource Management System built with Spring Boot, Hibernate JPA, Thymeleaf, and MySQL for Tech Mahindra to manage employees, IT resources, projects, and resource allocations.

## 🚀 Features

### Core Functionality
- **Employee Management**: Complete CRUD operations for employee data with department-wise organization
- **IT Resource Management**: Track hardware, software, and other IT assets with detailed specifications
- **Project Management**: Manage projects with timelines, budgets, and team assignments
- **Resource Allocation**: Assign employees and IT resources to projects with tracking and approval workflows
- **Dashboard**: Real-time analytics and insights with interactive charts and alerts
- **Reporting**: Generate comprehensive reports in various formats

### Key Capabilities
- **Search & Filter**: Advanced search across all entities with multiple filter options
- **Status Tracking**: Real-time status updates for all resources and allocations
- **Alert System**: Automated alerts for overdue allocations, warranty expirations, and project deadlines
- **Audit Trail**: Complete audit logging with creation and modification timestamps
- **Responsive Design**: Modern, mobile-friendly interface using Bootstrap 5
- **Data Validation**: Comprehensive client and server-side validation

## 🏗️ Architecture

### Technology Stack
- **Backend**: Spring Boot 3.2.0, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, Bootstrap 5, Chart.js, Font Awesome
- **Database**: MySQL 8.0+ (with H2 for testing)
- **Build Tool**: Maven
- **Java Version**: 17+

### Project Structure
```
src/
├── main/
│   ├── java/com/techmahindra/rms/
│   │   ├── controller/          # REST controllers and web controllers
│   │   ├── service/             # Business logic layer
│   │   ├── repository/          # Data access layer
│   │   ├── model/              # Entity classes
│   │   ├── config/             # Configuration classes
│   │   └── dto/                # Data Transfer Objects
│   └── resources/
│       ├── templates/          # Thymeleaf templates
│       ├── static/             # CSS, JS, images
│       └── application.properties
└── test/                       # Unit and integration tests
```

## 🛠️ Setup Instructions

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Setup
1. Install MySQL and create a database:
```sql
CREATE DATABASE tech_mahindra_rms;
CREATE USER 'rms_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON tech_mahindra_rms.* TO 'rms_user'@'localhost';
FLUSH PRIVILEGES;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tech_mahindra_rms?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=rms_user
spring.datasource.password=password
```

### Running the Application
1. Clone the repository:
```bash
git clone <repository-url>
cd resource-management-system
```

2. Build the application:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Access the application at: `http://localhost:8080/rms`

### Default Login
- Username: `admin`
- Password: `admin123`

## 📊 Database Schema

### Core Entities
- **Employee**: Personal info, department, designation, skills, status
- **ITResource**: Hardware/software assets with specifications and warranty info
- **Project**: Project details, timelines, budget, team size
- **ResourceAllocation**: Links employees and resources to projects with dates and status

### Key Relationships
- One-to-Many: Employee → ResourceAllocations
- One-to-Many: Project → ResourceAllocations
- One-to-Many: ITResource → ResourceAllocations
- Many-to-One: ResourceAllocation → Employee/Project/ITResource

## 🎯 Usage Guide

### Dashboard
- View real-time statistics and metrics
- Monitor critical alerts and overdue items
- Access quick action buttons for common tasks
- Analyze data through interactive charts

### Employee Management
- Add/Edit/Delete employees
- Search by name, ID, department, or skills
- Track employee status and allocations
- Generate employee reports

### IT Resource Management
- Manage hardware and software inventory
- Track warranty expiration dates
- Monitor resource allocation status
- Generate resource utilization reports

### Project Management
- Create and manage projects
- Set timelines and budgets
- Track project status and progress
- Assign team members and resources

### Resource Allocation
- Allocate employees and resources to projects
- Set allocation dates and expected return dates
- Approve/reject allocation requests
- Track overdue allocations

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/rms

# Database Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Thymeleaf Configuration
spring.thymeleaf.cache=false

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123
```

### Customization
- **Branding**: Update logo and colors in `custom.css`
- **Email Templates**: Modify Thymeleaf templates in `src/main/resources/templates/`
- **Business Rules**: Adjust validation logic in service classes
- **Reports**: Customize report generation in service layer

## 📈 Features in Detail

### Employee Management
- **Fields**: ID, Name, Email, Phone, Department, Designation, Manager, Status, Join Date, Skills, Experience, Location
- **Validations**: Email format, phone number, unique employee ID
- **Search**: Multi-field search with pagination
- **Status**: Active, Inactive, On Leave, Terminated

### IT Resource Management
- **Types**: Hardware, Software, Network, Storage, Security, Cloud
- **Categories**: Laptops, Desktops, Servers, Mobile devices, Software licenses, etc.
- **Tracking**: Purchase date, warranty, vendor, specifications, location
- **Status**: Available, Allocated, Maintenance, Retired, Lost, Damaged

### Project Management
- **Fields**: Code, Name, Description, Client, Manager, Dates, Status, Priority, Budget, Technology Stack
- **Status**: Planning, Active, On Hold, Completed, Cancelled, Delayed
- **Priority**: Low, Medium, High, Critical
- **Tracking**: Start/end dates, planned vs actual timelines

### Resource Allocation
- **Types**: Permanent, Temporary, Shared
- **Status**: Active, Returned, Overdue, Pending Approval, Rejected
- **Workflow**: Request → Approval → Active → Return
- **Tracking**: Allocation percentage, notes, approval history

## 🚨 Alerts & Notifications

### Automated Alerts
- **Overdue Allocations**: Resources not returned on time
- **Warranty Expiration**: IT resources with expiring warranties
- **Project Deadlines**: Projects approaching end dates
- **Resource Shortages**: Departments with low resource availability

### Dashboard Indicators
- Color-coded status indicators
- Real-time metric updates
- Critical alert badges
- Recent activity feeds

## 📊 Reporting Features

### Report Types
- Employee reports by department, status, skills
- Resource utilization and availability reports
- Project status and timeline reports
- Allocation history and trends

### Export Formats
- PDF reports with company branding
- Excel spreadsheets for data analysis
- CSV files for external systems
- Print-friendly web views

## 🔒 Security Features

### Authentication & Authorization
- Spring Security integration
- Role-based access control
- Session management
- Password encryption

### Data Protection
- Input validation and sanitization
- SQL injection prevention
- XSS protection
- CSRF token validation

## 🛡️ Best Practices

### Code Quality
- Repository pattern for data access
- Service layer for business logic
- DTO pattern for data transfer
- Comprehensive error handling

### Performance
- Lazy loading for entity relationships
- Pagination for large datasets
- Database indexing on key fields
- Optimized queries with JPA

### Maintainability
- Clear separation of concerns
- Comprehensive logging
- Unit and integration tests
- Documentation and comments

## 🚀 Future Enhancements

### Planned Features
- **Mobile App**: Native mobile application
- **API Integration**: RESTful APIs for external systems
- **Advanced Analytics**: Machine learning insights
- **Workflow Engine**: Configurable approval workflows
- **Multi-tenant Support**: Support for multiple organizations
- **Real-time Notifications**: WebSocket-based notifications

### Technical Improvements
- **Microservices Architecture**: Break down into smaller services
- **Cloud Deployment**: AWS/Azure deployment configurations
- **Docker Support**: Containerization for easy deployment
- **Performance Monitoring**: Application performance monitoring
- **Automated Testing**: Comprehensive test coverage

## 📞 Support

For technical support or questions:
- **Email**: support@techmahindra.com
- **Documentation**: Internal wiki and knowledge base
- **Issue Tracking**: JIRA project for bug reports and feature requests

## 📄 License

Copyright © 2024 Tech Mahindra Limited. All rights reserved.

This software is proprietary and confidential to Tech Mahindra Limited.

---

**Version**: 1.0.0  
**Last Updated**: December 2024  
**Maintained By**: Tech Mahindra IT Team