package com.kt.leave.controller;

import com.kt.leave.dto.LeaveDtos.*;
import com.kt.leave.model.Holiday;
import com.kt.leave.repository.HolidayRepository;
import com.kt.leave.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired private LeaveService leaveService;

    @GetMapping("/leave-summary")
    public ResponseEntity<List<LeaveReportResponse>> summary() {
        return ResponseEntity.ok(leaveService.getLeaveReport());
    }
}
