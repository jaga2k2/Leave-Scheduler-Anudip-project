package com.kt.leave.dto;

import com.kt.leave.model.LeaveApplication;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveDtos {

    public static class LeaveApplicationRequest {
        private LeaveApplication.LeaveType leaveType;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String reason;
        private String addressDuringLeave;
        private String superiorEmail;

        public LeaveApplicationRequest() {
        }

        public LeaveApplication.LeaveType getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(LeaveApplication.LeaveType leaveType) {
            this.leaveType = leaveType;
        }

        public LocalDate getFromDate() {
            return fromDate;
        }

        public void setFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
        }

        public LocalDate getToDate() {
            return toDate;
        }

        public void setToDate(LocalDate toDate) {
            this.toDate = toDate;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getAddressDuringLeave() {
            return addressDuringLeave;
        }

        public void setAddressDuringLeave(String addressDuringLeave) {
            this.addressDuringLeave = addressDuringLeave;
        }

        public String getSuperiorEmail() {
            return superiorEmail;
        }

        public void setSuperiorEmail(String superiorEmail) {
            this.superiorEmail = superiorEmail;
        }
    }

    public static class LeaveApplicationResponse {
        private Long id;
        private String employeeName;
        private String employeeId;
        private String department;
        private LeaveApplication.LeaveType leaveType;
        private LocalDate fromDate;
        private LocalDate toDate;
        private int numberOfDays;
        private String reason;
        private String addressDuringLeave;
        private String superiorEmail;
        private LeaveApplication.LeaveStatus status;
        private String managerRemarks;
        private String approvedBy;
        private LocalDateTime appliedAt;
        private LocalDateTime updatedAt;
        private boolean withdrawn;

        public LeaveApplicationResponse() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public LeaveApplication.LeaveType getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(LeaveApplication.LeaveType leaveType) {
            this.leaveType = leaveType;
        }

        public LocalDate getFromDate() {
            return fromDate;
        }

        public void setFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
        }

        public LocalDate getToDate() {
            return toDate;
        }

        public void setToDate(LocalDate toDate) {
            this.toDate = toDate;
        }

        public int getNumberOfDays() {
            return numberOfDays;
        }

        public void setNumberOfDays(int numberOfDays) {
            this.numberOfDays = numberOfDays;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getAddressDuringLeave() {
            return addressDuringLeave;
        }

        public void setAddressDuringLeave(String addressDuringLeave) {
            this.addressDuringLeave = addressDuringLeave;
        }

        public String getSuperiorEmail() {
            return superiorEmail;
        }

        public void setSuperiorEmail(String superiorEmail) {
            this.superiorEmail = superiorEmail;
        }

        public LeaveApplication.LeaveStatus getStatus() {
            return status;
        }

        public void setStatus(LeaveApplication.LeaveStatus status) {
            this.status = status;
        }

        public String getManagerRemarks() {
            return managerRemarks;
        }

        public void setManagerRemarks(String managerRemarks) {
            this.managerRemarks = managerRemarks;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public LocalDateTime getAppliedAt() {
            return appliedAt;
        }

        public void setAppliedAt(LocalDateTime appliedAt) {
            this.appliedAt = appliedAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

        public boolean isWithdrawn() {
            return withdrawn;
        }

        public void setWithdrawn(boolean withdrawn) {
            this.withdrawn = withdrawn;
        }

        public static class Builder {
            private final LeaveApplicationResponse instance = new LeaveApplicationResponse();

            public Builder id(Long id) { instance.setId(id); return this; }
            public Builder employeeName(String employeeName) { instance.setEmployeeName(employeeName); return this; }
            public Builder employeeId(String employeeId) { instance.setEmployeeId(employeeId); return this; }
            public Builder department(String department) { instance.setDepartment(department); return this; }
            public Builder leaveType(LeaveApplication.LeaveType leaveType) { instance.setLeaveType(leaveType); return this; }
            public Builder fromDate(LocalDate fromDate) { instance.setFromDate(fromDate); return this; }
            public Builder toDate(LocalDate toDate) { instance.setToDate(toDate); return this; }
            public Builder numberOfDays(int numberOfDays) { instance.setNumberOfDays(numberOfDays); return this; }
            public Builder reason(String reason) { instance.setReason(reason); return this; }
            public Builder addressDuringLeave(String addressDuringLeave) { instance.setAddressDuringLeave(addressDuringLeave); return this; }
            public Builder superiorEmail(String superiorEmail) { instance.setSuperiorEmail(superiorEmail); return this; }
            public Builder status(LeaveApplication.LeaveStatus status) { instance.setStatus(status); return this; }
            public Builder managerRemarks(String managerRemarks) { instance.setManagerRemarks(managerRemarks); return this; }
            public Builder approvedBy(String approvedBy) { instance.setApprovedBy(approvedBy); return this; }
            public Builder appliedAt(LocalDateTime appliedAt) { instance.setAppliedAt(appliedAt); return this; }
            public Builder updatedAt(LocalDateTime updatedAt) { instance.setUpdatedAt(updatedAt); return this; }
            public Builder withdrawn(boolean withdrawn) { instance.setWithdrawn(withdrawn); return this; }
            public LeaveApplicationResponse build() { return instance; }
        }
    }

    public static class LeaveActionRequest {
        private String action; // APPROVE or REJECT
        private String remarks;

        public LeaveActionRequest() {
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }

    public static class LeaveBalanceResponse {
        private int annualTotal;
        private int annualUsed;
        private int annualBalance;
        private int sickTotal;
        private int sickUsed;
        private int sickBalance;
        private int casualTotal;
        private int casualUsed;
        private int casualBalance;
        private int year;

        public LeaveBalanceResponse() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public int getAnnualTotal() { return annualTotal; }
        public void setAnnualTotal(int annualTotal) { this.annualTotal = annualTotal; }
        public int getAnnualUsed() { return annualUsed; }
        public void setAnnualUsed(int annualUsed) { this.annualUsed = annualUsed; }
        public int getAnnualBalance() { return annualBalance; }
        public void setAnnualBalance(int annualBalance) { this.annualBalance = annualBalance; }
        public int getSickTotal() { return sickTotal; }
        public void setSickTotal(int sickTotal) { this.sickTotal = sickTotal; }
        public int getSickUsed() { return sickUsed; }
        public void setSickUsed(int sickUsed) { this.sickUsed = sickUsed; }
        public int getSickBalance() { return sickBalance; }
        public void setSickBalance(int sickBalance) { this.sickBalance = sickBalance; }
        public int getCasualTotal() { return casualTotal; }
        public void setCasualTotal(int casualTotal) { this.casualTotal = casualTotal; }
        public int getCasualUsed() { return casualUsed; }
        public void setCasualUsed(int casualUsed) { this.casualUsed = casualUsed; }
        public int getCasualBalance() { return casualBalance; }
        public void setCasualBalance(int casualBalance) { this.casualBalance = casualBalance; }
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }

        public static class Builder {
            private final LeaveBalanceResponse instance = new LeaveBalanceResponse();

            public Builder annualTotal(int annualTotal) { instance.setAnnualTotal(annualTotal); return this; }
            public Builder annualUsed(int annualUsed) { instance.setAnnualUsed(annualUsed); return this; }
            public Builder annualBalance(int annualBalance) { instance.setAnnualBalance(annualBalance); return this; }
            public Builder sickTotal(int sickTotal) { instance.setSickTotal(sickTotal); return this; }
            public Builder sickUsed(int sickUsed) { instance.setSickUsed(sickUsed); return this; }
            public Builder sickBalance(int sickBalance) { instance.setSickBalance(sickBalance); return this; }
            public Builder casualTotal(int casualTotal) { instance.setCasualTotal(casualTotal); return this; }
            public Builder casualUsed(int casualUsed) { instance.setCasualUsed(casualUsed); return this; }
            public Builder casualBalance(int casualBalance) { instance.setCasualBalance(casualBalance); return this; }
            public Builder year(int year) { instance.setYear(year); return this; }
            public LeaveBalanceResponse build() { return instance; }
        }
    }

    public static class LeaveReportResponse {
        private String employeeName;
        private String department;
        private int totalLeavesTaken;
        private int annualTaken;
        private int sickTaken;
        private int casualTaken;
        private int pendingCount;

        public LeaveReportResponse() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public int getTotalLeavesTaken() { return totalLeavesTaken; }
        public void setTotalLeavesTaken(int totalLeavesTaken) { this.totalLeavesTaken = totalLeavesTaken; }
        public int getAnnualTaken() { return annualTaken; }
        public void setAnnualTaken(int annualTaken) { this.annualTaken = annualTaken; }
        public int getSickTaken() { return sickTaken; }
        public void setSickTaken(int sickTaken) { this.sickTaken = sickTaken; }
        public int getCasualTaken() { return casualTaken; }
        public void setCasualTaken(int casualTaken) { this.casualTaken = casualTaken; }
        public int getPendingCount() { return pendingCount; }
        public void setPendingCount(int pendingCount) { this.pendingCount = pendingCount; }

        public static class Builder {
            private final LeaveReportResponse instance = new LeaveReportResponse();

            public Builder employeeName(String employeeName) { instance.setEmployeeName(employeeName); return this; }
            public Builder department(String department) { instance.setDepartment(department); return this; }
            public Builder totalLeavesTaken(int totalLeavesTaken) { instance.setTotalLeavesTaken(totalLeavesTaken); return this; }
            public Builder annualTaken(int annualTaken) { instance.setAnnualTaken(annualTaken); return this; }
            public Builder sickTaken(int sickTaken) { instance.setSickTaken(sickTaken); return this; }
            public Builder casualTaken(int casualTaken) { instance.setCasualTaken(casualTaken); return this; }
            public Builder pendingCount(int pendingCount) { instance.setPendingCount(pendingCount); return this; }
            public LeaveReportResponse build() { return instance; }
        }
    }
}
