package com.kt.leave.service;

import com.kt.leave.model.LeaveApplication;
import com.kt.leave.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    @Nullable
    private JavaMailSender mailSender;
    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Async
    public void sendLeaveApplicationNotification(LeaveApplication application) {
        if (!mailEnabled || mailSender == null) return;
        User employee = application.getEmployee();
        String managerEmail = employee.getManager() != null ? employee.getManager().getEmail() : application.getSuperiorEmail();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(managerEmail);
        msg.setSubject("Leave Application from " + employee.getFirstName() + " " + employee.getLastName());
        msg.setText(String.format(
            "Dear Manager,\n\n%s %s has applied for %s leave from %s to %s (%d days).\nReason: %s\n\nPlease login to the Leave Scheduler to take action.\n\nRegards,\nLeave Scheduler System",
            employee.getFirstName(), employee.getLastName(),
            application.getLeaveType(),
            application.getFromDate(), application.getToDate(),
            application.getNumberOfDays(),
            application.getReason()
        ));
        try { mailSender.send(msg); } catch (Exception e) { /* log */ }
    }

    @Async
    public void sendLeaveStatusNotification(LeaveApplication application) {
        if (!mailEnabled || mailSender == null) return;
        User employee = application.getEmployee();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(employee.getEmail());
        if (application.getSuperiorEmail() != null && !application.getSuperiorEmail().isEmpty()) {
            msg.setCc(application.getSuperiorEmail());
        }
        msg.setSubject("Leave Application " + application.getStatus().name());
        msg.setText(String.format(
            "Dear %s,\n\nYour leave application from %s to %s has been %s.\n%s\n\nRegards,\nLeave Scheduler System",
            employee.getFirstName(),
            application.getFromDate(), application.getToDate(),
            application.getStatus().name(),
            application.getManagerRemarks() != null ? "Manager Remarks: " + application.getManagerRemarks() : ""
        ));
        try { mailSender.send(msg); } catch (Exception e) { /* log */ }
    }

    @Async
    public void sendLeaveBalanceCreditNotification(User user, int days, String leaveType) {
        if (!mailEnabled || mailSender == null) return;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("Leave Balance Credited");
        msg.setText(String.format(
            "Dear %s,\n\n%d days of %s leave have been credited to your account.\n\nRegards,\nLeave Scheduler System",
            user.getFirstName(), days, leaveType
        ));
        try { mailSender.send(msg); } catch (Exception e) { /* log */ }
    }
}
