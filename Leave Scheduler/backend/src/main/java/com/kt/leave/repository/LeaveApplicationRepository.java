package com.kt.leave.repository;

import com.kt.leave.model.LeaveApplication;
import com.kt.leave.model.LeaveApplication.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> findByEmployeeId(Long employeeId);
    List<LeaveApplication> findByEmployeeIdOrderByAppliedAtDesc(Long employeeId);
    List<LeaveApplication> findByStatus(LeaveStatus status);

    @Query("SELECT l FROM LeaveApplication l WHERE l.employee.manager.id = :managerId AND l.status = 'PENDING'")
    List<LeaveApplication> findPendingByManagerId(Long managerId);

    @Query("SELECT l FROM LeaveApplication l WHERE l.employee.manager.id = :managerId")
    List<LeaveApplication> findAllByManagerId(Long managerId);

    @Query("SELECT l FROM LeaveApplication l WHERE l.status = 'PENDING' AND l.appliedAt < :cutoffDate")
    List<LeaveApplication> findPendingOlderThan(java.time.LocalDateTime cutoffDate);

    List<LeaveApplication> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    @Query("SELECT l FROM LeaveApplication l WHERE l.employee.id = :empId AND " +
           "((l.fromDate BETWEEN :from AND :to) OR (l.toDate BETWEEN :from AND :to))")
    List<LeaveApplication> findOverlapping(Long empId, LocalDate from, LocalDate to);
}
