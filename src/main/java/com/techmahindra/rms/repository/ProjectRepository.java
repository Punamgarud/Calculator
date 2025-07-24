package com.techmahindra.rms.repository;

import com.techmahindra.rms.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    Optional<Project> findByProjectCode(String projectCode);
    
    List<Project> findByStatus(Project.ProjectStatus status);
    
    List<Project> findByPriority(Project.Priority priority);
    
    List<Project> findByProjectManager(String projectManager);
    
    List<Project> findByClientName(String clientName);
    
    List<Project> findByLocation(String location);
    
    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.projectCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.clientName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.projectManager) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Project> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    Long countByStatus(@Param("status") Project.ProjectStatus status);
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.priority = :priority")
    Long countByPriority(@Param("priority") Project.Priority priority);
    
    @Query("SELECT p FROM Project p WHERE p.endDate BETWEEN :startDate AND :endDate")
    List<Project> findProjectsEndingBetween(@Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM Project p WHERE p.startDate BETWEEN :startDate AND :endDate")
    List<Project> findProjectsStartingBetween(@Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM Project p WHERE p.plannedEndDate < :currentDate AND p.status != 'COMPLETED'")
    List<Project> findOverdueProjects(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT p FROM Project p WHERE p.plannedEndDate BETWEEN :now AND :futureDate AND p.status = 'ACTIVE'")
    List<Project> findProjectsEndingSoon(@Param("now") LocalDate now, 
                                        @Param("futureDate") LocalDate futureDate);
    
    @Query("SELECT DISTINCT p.clientName FROM Project p WHERE p.clientName IS NOT NULL ORDER BY p.clientName")
    List<String> findAllClients();
    
    @Query("SELECT DISTINCT p.projectManager FROM Project p ORDER BY p.projectManager")
    List<String> findAllProjectManagers();
    
    @Query("SELECT DISTINCT p.location FROM Project p WHERE p.location IS NOT NULL ORDER BY p.location")
    List<String> findAllProjectLocations();
    
    @Query("SELECT p FROM Project p WHERE p.status IN ('ACTIVE', 'PLANNING')")
    List<Project> findActiveProjects();
}