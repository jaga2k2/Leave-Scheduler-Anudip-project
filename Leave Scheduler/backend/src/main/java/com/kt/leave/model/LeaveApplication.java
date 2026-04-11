package com.kt.leave.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_applications")
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    private int numberOfDays;

    @Column(length = 500)
    private String reason;

    @Column(length = 500)
    private String addressDuringLeave;

    private String superiorEmail;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    private String managerRemarks;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean withdrawn = false;

    // ===================== CONSTRUCTORS =====================

    public LeaveApplication() {
    }

    public LeaveApplication(Long id, User employee, LeaveType leaveType,
                            LocalDate fromDate, LocalDate toDate, int numberOfDays,
                            String reason, String addressDuringLeave, String superiorEmail,
                            LeaveStatus status, String managerRemarks, User approvedBy,
                            LocalDateTime appliedAt, LocalDateTime updatedAt, boolean withdrawn) {
        this.id = id;
        this.employee = employee;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.addressDuringLeave = addressDuringLeave;
        this.superiorEmail = superiorEmail;
        this.status = status;
        this.managerRemarks = managerRemarks;
        this.approvedBy = approvedBy;
        this.appliedAt = appliedAt;
        this.updatedAt = updatedAt;
        this.withdrawn = withdrawn;
    }

    // ===================== GETTERS =====================

    public Long getId() { return id; }
    public User getEmployee() { return employee; }
    public LeaveType getLeaveType() { return leaveType; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }
    public int getNumberOfDays() { return numberOfDays; }
    public String getReason() { return reason; }
    public String getAddressDuringLeave() { return addressDuringLeave; }
    public String getSuperiorEmail() { return superiorEmail; }
    public LeaveStatus getStatus() { return status; }
    public String getManagerRemarks() { return managerRemarks; }
    public User getApprovedBy() { return approvedBy; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isWithdrawn() { return withdrawn; }

    // ===================== SETTERS =====================

    public void setId(Long id) { this.id = id; }
    public void setEmployee(User employee) { this.employee = employee; }
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
    public void setNumberOfDays(int numberOfDays) { this.numberOfDays = numberOfDays; }
    public void setReason(String reason) { this.reason = reason; }
    public void setAddressDuringLeave(String addressDuringLeave) { this.addressDuringLeave = addressDuringLeave; }
    public void setSuperiorEmail(String superiorEmail) { this.superiorEmail = superiorEmail; }
    public void setStatus(LeaveStatus status) { this.status = status; }
    public void setManagerRemarks(String managerRemarks) { this.managerRemarks = managerRemarks; }
    public void setApprovedBy(User approvedBy) { this.approvedBy = approvedBy; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setWithdrawn(boolean withdrawn) { this.withdrawn = withdrawn; }

    // ===================== LIFECYCLE METHODS =====================

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = LeaveStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===================== ENUMS =====================

    public enum LeaveType {
        ANNUAL, SICK, CASUAL, MATERNITY, PATERNITY, UNPAID
    }

    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED, WITHDRAWN, AUTO_APPROVED
    }
}