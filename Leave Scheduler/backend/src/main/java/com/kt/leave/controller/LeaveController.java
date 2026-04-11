package com.kt.leave.controller;

import com.kt.leave.dto.LeaveDtos.*;
import com.kt.leave.model.User;
import com.kt.leave.repository.UserRepository;
import com.kt.leave.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired private LeaveService leaveService;
    @Autowired private UserRepository userRepo;

    private Long getUserId(UserDetails ud) {
        return userRepo.findByUsername(ud.getUsername()).orElseThrow().getId();
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveApplicationResponse> apply(
            @AuthenticationPrincipal UserDetails ud,
            @RequestBody LeaveApplicationRequest req) {
        return ResponseEntity.ok(leaveService.applyLeave(req, getUserId(ud)));
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<LeaveApplicationResponse> withdraw(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(leaveService.withdrawLeave(id, getUserId(ud)));
    }

    @GetMapping("/my")
    public ResponseEntity<List<LeaveApplicationResponse>> myLeaves(
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(leaveService.getMyLeaves(getUserId(ud)));
    }

    @GetMapping("/balance")
    public ResponseEntity<LeaveBalanceResponse> myBalance(
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(leaveService.getBalance(getUserId(ud)));
    }
}
