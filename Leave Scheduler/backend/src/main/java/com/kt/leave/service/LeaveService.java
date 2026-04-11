package com.kt.leave.service;

import com.kt.leave.dto.LeaveDtos.*;
import com.kt.leave.model.*;
import com.kt.leave.model.LeaveApplication.LeaveStatus;
import com.kt.leave.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired private LeaveApplicationRepository leaveRepo;
    @Autowired private LeaveBalanceRepository balanceRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private HolidayRepository holidayRepo;
    @Autowired private EmailService emailService;

    @Value("${app.leave.annual-days:21}")
    private int annualDays;
    @Value("${app.leave.sick-days:10}")
    private int sickDays;
    @Value("${app.leave.casual-days:7}")
    private int casualDays;

    // ---- APPLY ----
    @Transactional
    public LeaveApplicationResponse applyLeave(LeaveApplicationRequest req, Long employeeId) {
        User employee = userRepo.findById(employeeId).orElseThrow();
        int days = countWorkingDays(req.getFromDate(), req.getToDate());

        LeaveBalance balance = balanceRepo.findByUserId(employeeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        validateBalance(balance, req.getLeaveType(), days);

        List<LeaveApplication> overlapping = leaveRepo.findOverlapping(employeeId, req.getFromDate(), req.getToDate());
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("You have an overlapping leave application");
        }

        LeaveApplication app = new LeaveApplication();
        app.setEmployee(employee);
        app.setLeaveType(req.getLeaveType());
        app.setFromDate(req.getFromDate());
        app.setToDate(req.getToDate());
        app.setNumberOfDays(days);
        app.setReason(req.getReason());
        app.setAddressDuringLeave(req.getAddressDuringLeave());
        app.setSuperiorEmail(req.getSuperiorEmail());
        app.setStatus(LeaveStatus.PENDING);
        app.setWithdrawn(false);

        app = leaveRepo.save(app);
        emailService.sendLeaveApplicationNotification(app);
        return toResponse(app);
    }

    // ---- WITHDRAW ----
    @Transactional
    public LeaveApplicationResponse withdrawLeave(Long leaveId, Long employeeId) {
        LeaveApplication app = leaveRepo.findById(leaveId).orElseThrow();
        if (!app.getEmployee().getId().equals(employeeId)) throw new RuntimeException("Unauthorized");
        if (app.getStatus() == LeaveStatus.APPROVED) {
            restoreBalance(app);
        }
        app.setStatus(LeaveStatus.WITHDRAWN);
        app.setWithdrawn(true);
        app = leaveRepo.save(app);
        emailService.sendLeaveStatusNotification(app);
        return toResponse(app);
    }

    // ---- MANAGER ACTION ----
    @Transactional
    public LeaveApplicationResponse actOnLeave(Long leaveId, LeaveActionRequest req, Long managerId) {
        LeaveApplication app = leaveRepo.findById(leaveId).orElseThrow();
        User manager = userRepo.findById(managerId).orElseThrow();
        boolean isAdmin = manager.getRole() == User.Role.ADMIN;
        boolean isAssignedManager = app.getEmployee().getManager() != null
                && app.getEmployee().getManager().getId().equals(managerId);

        if (!isAdmin && !isAssignedManager) {
            throw new RuntimeException("You are not allowed to act on this leave request");
        }

        if (!"APPROVE".equalsIgnoreCase(req.getAction()) && !"REJECT".equalsIgnoreCase(req.getAction())) {
            throw new RuntimeException("Invalid action");
        }

        if ("APPROVE".equalsIgnoreCase(req.getAction())) {
            app.setStatus(LeaveStatus.APPROVED);
            deductBalance(app);
        } else {
            app.setStatus(LeaveStatus.REJECTED);
        }

        app.setManagerRemarks(req.getRemarks());
        app.setApprovedBy(manager);
        app = leaveRepo.save(app);
        emailService.sendLeaveStatusNotification(app);
        return toResponse(app);
    }

    // ---- AUTO APPROVE (scheduled) ----
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoApprovePendingLeaves() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);
        List<LeaveApplication> pending = leaveRepo.findPendingOlderThan(cutoff);
        for (LeaveApplication app : pending) {
            app.setStatus(LeaveStatus.AUTO_APPROVED);
            app.setManagerRemarks("Auto-approved after 3 days");
            deductBalance(app);
            leaveRepo.save(app);
            emailService.sendLeaveStatusNotification(app);
        }
    }

    // ---- CREDIT LEAVE (annual) ----
    @Scheduled(cron = "0 0 1 1 1 *") // Jan 1st every year
    @Transactional
    public void creditAnnualLeave() {
        List<User> users = userRepo.findAll();
        int year = LocalDate.now().getYear();
        for (User user : users) {
            LeaveBalance balance = balanceRepo.findByUserId(user.getId()).orElseGet(() -> {
                LeaveBalance newBalance = new LeaveBalance();
                newBalance.setUser(user);
                return newBalance;
            });
            balance.setAnnualBalance(annualDays);
            balance.setSickBalance(sickDays);
            balance.setCasualBalance(casualDays);
            balance.setAnnualUsed(0);
            balance.setSickUsed(0);
            balance.setCasualUsed(0);
            balance.setLastCreditedDate(LocalDate.now());
            balance.setYear(year);
            balanceRepo.save(balance);
            emailService.sendLeaveBalanceCreditNotification(user, annualDays, "Annual");
        }
    }

    // ---- QUERIES ----
    public List<LeaveApplicationResponse> getMyLeaves(Long employeeId) {
        return leaveRepo.findByEmployeeIdOrderByAppliedAtDesc(employeeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LeaveApplicationResponse> getTeamLeaves(Long managerId) {
        User user = userRepo.findById(managerId).orElseThrow();
        List<LeaveApplication> applications = user.getRole() == User.Role.ADMIN
                ? leaveRepo.findAll()
                : leaveRepo.findAllByManagerId(managerId);
        return applications
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LeaveApplicationResponse> getPendingApprovalsForManager(Long managerId) {
        User user = userRepo.findById(managerId).orElseThrow();
        List<LeaveApplication> applications = user.getRole() == User.Role.ADMIN
                ? leaveRepo.findByStatus(LeaveStatus.PENDING)
                : leaveRepo.findPendingByManagerId(managerId);
        return applications
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LeaveBalanceResponse getBalance(Long userId) {
        LeaveBalance b = balanceRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Balance not found"));
        return LeaveBalanceResponse.builder()
                .annualTotal(annualDays).annualUsed(b.getAnnualUsed()).annualBalance(b.getAnnualBalance())
                .sickTotal(sickDays).sickUsed(b.getSickUsed()).sickBalance(b.getSickBalance())
                .casualTotal(casualDays).casualUsed(b.getCasualUsed()).casualBalance(b.getCasualBalance())
                .year(b.getYear())
                .build();
    }

    public List<LeaveReportResponse> getLeaveReport() {
        return userRepo.findAll().stream().map(user -> {
            List<LeaveApplication> apps = leaveRepo.findByEmployeeId(user.getId());
            long annual = apps.stream().filter(a -> a.getLeaveType() == LeaveApplication.LeaveType.ANNUAL && a.getStatus() == LeaveStatus.APPROVED).mapToLong(LeaveApplication::getNumberOfDays).sum();
            long sick = apps.stream().filter(a -> a.getLeaveType() == LeaveApplication.LeaveType.SICK && a.getStatus() == LeaveStatus.APPROVED).mapToLong(LeaveApplication::getNumberOfDays).sum();
            long casual = apps.stream().filter(a -> a.getLeaveType() == LeaveApplication.LeaveType.CASUAL && a.getStatus() == LeaveStatus.APPROVED).mapToLong(LeaveApplication::getNumberOfDays).sum();
            long pending = apps.stream().filter(a -> a.getStatus() == LeaveStatus.PENDING).count();
            return LeaveReportResponse.builder()
                    .employeeName(user.getFirstName() + " " + user.getLastName())
                    .department(user.getDepartment() != null ? user.getDepartment().name() : "")
                    .totalLeavesTaken((int)(annual+sick+casual))
                    .annualTaken((int)annual).sickTaken((int)sick).casualTaken((int)casual)
                    .pendingCount((int)pending)
                    .build();
        }).collect(Collectors.toList());
    }

    // ---- HELPERS ----
    private int countWorkingDays(LocalDate from, LocalDate to) {
        List<LocalDate> holidays = holidayRepo.findByDateBetweenOrderByDate(from, to)
                .stream().map(Holiday::getDate).collect(Collectors.toList());
        int count = 0;
        LocalDate d = from;
        while (!d.isAfter(to)) {
            if (d.getDayOfWeek() != DayOfWeek.SATURDAY &&
                d.getDayOfWeek() != DayOfWeek.SUNDAY &&
                !holidays.contains(d)) {
                count++;
            }
            d = d.plusDays(1);
        }
        return count;
    }

    private void validateBalance(LeaveBalance balance, LeaveApplication.LeaveType type, int days) {
        switch (type) {
            case ANNUAL -> { if (balance.getAnnualBalance() < days) throw new RuntimeException("Insufficient annual leave balance"); }
            case SICK   -> { if (balance.getSickBalance() < days) throw new RuntimeException("Insufficient sick leave balance"); }
            case CASUAL -> { if (balance.getCasualBalance() < days) throw new RuntimeException("Insufficient casual leave balance"); }
        }
    }

    private void deductBalance(LeaveApplication app) {
        LeaveBalance balance = balanceRepo.findByUserId(app.getEmployee().getId()).orElseThrow();
        int days = app.getNumberOfDays();
        switch (app.getLeaveType()) {
            case ANNUAL -> { balance.setAnnualUsed(balance.getAnnualUsed() + days); balance.setAnnualBalance(balance.getAnnualBalance() - days); }
            case SICK   -> { balance.setSickUsed(balance.getSickUsed() + days); balance.setSickBalance(balance.getSickBalance() - days); }
            case CASUAL -> { balance.setCasualUsed(balance.getCasualUsed() + days); balance.setCasualBalance(balance.getCasualBalance() - days); }
        }
        balanceRepo.save(balance);
    }

    private void restoreBalance(LeaveApplication app) {
        LeaveBalance balance = balanceRepo.findByUserId(app.getEmployee().getId()).orElseThrow();
        int days = app.getNumberOfDays();
        switch (app.getLeaveType()) {
            case ANNUAL -> { balance.setAnnualUsed(balance.getAnnualUsed() - days); balance.setAnnualBalance(balance.getAnnualBalance() + days); }
            case SICK   -> { balance.setSickUsed(balance.getSickUsed() - days); balance.setSickBalance(balance.getSickBalance() + days); }
            case CASUAL -> { balance.setCasualUsed(balance.getCasualUsed() - days); balance.setCasualBalance(balance.getCasualBalance() + days); }
        }
        balanceRepo.save(balance);
    }

    private LeaveApplicationResponse toResponse(LeaveApplication app) {
        return LeaveApplicationResponse.builder()
                .id(app.getId())
                .employeeName(app.getEmployee().getFirstName() + " " + app.getEmployee().getLastName())
                .employeeId(app.getEmployee().getUsername())
                .department(app.getEmployee().getDepartment() != null ? app.getEmployee().getDepartment().name() : "")
                .leaveType(app.getLeaveType())
                .fromDate(app.getFromDate())
                .toDate(app.getToDate())
                .numberOfDays(app.getNumberOfDays())
                .reason(app.getReason())
                .addressDuringLeave(app.getAddressDuringLeave())
                .superiorEmail(app.getSuperiorEmail())
                .status(app.getStatus())
                .managerRemarks(app.getManagerRemarks())
                .approvedBy(app.getApprovedBy() != null ? app.getApprovedBy().getFirstName() + " " + app.getApprovedBy().getLastName() : null)
                .appliedAt(app.getAppliedAt())
                .updatedAt(app.getUpdatedAt())
                .withdrawn(app.isWithdrawn())
                .build();
    }
}
