package com.kt.leave.controller;

import com.kt.leave.dto.LeaveDtos.*;
import com.kt.leave.repository.UserRepository;
import com.kt.leave.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired private LeaveService leaveService;
    @Autowired private UserRepository userRepo;

    private Long getUserId(UserDetails ud) {
        return userRepo.findByUsername(ud.getUsername()).orElseThrow().getId();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveApplicationResponse>> pending(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(leaveService.getPendingApprovalsForManager(getUserId(ud)));
    }

    @GetMapping("/team-leaves")
    public ResponseEntity<List<LeaveApplicationResponse>> teamLeaves(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(leaveService.getTeamLeaves(getUserId(ud)));
    }

    @PutMapping("/leaves/{id}/action")
    public ResponseEntity<LeaveApplicationResponse> action(
            @PathVariable Long id,
            @RequestBody LeaveActionRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(leaveService.actOnLeave(id, req, getUserId(ud)));
    }
}
