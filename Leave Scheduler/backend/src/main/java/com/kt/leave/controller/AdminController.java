package com.kt.leave.controller;

import com.kt.leave.model.*;
import com.kt.leave.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private UserRepository userRepo;
    @Autowired private LeaveBalanceRepository balanceRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${app.leave.annual-days:21}")
    private int annualDays;

    @Value("${app.leave.sick-days:10}")
    private int sickDays;

    @Value("${app.leave.casual-days:7}")
    private int casualDays;

    // ===== GET ALL USERS =====
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    // ===== CREATE USER =====
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> body) {
        User user = new User();
        user.setUsername((String) body.get("username"));
        user.setPassword(passwordEncoder.encode((String) body.get("password")));
        user.setEmail((String) body.get("email"));
        user.setFirstName((String) body.get("firstName"));
        user.setLastName((String) body.get("lastName"));
        user.setRole(User.Role.valueOf((String) body.get("role")));
        user.setDepartment(body.get("department") != null
                ? User.Department.valueOf((String) body.get("department"))
                : null);
        user.setDesignation((String) body.get("designation"));
        user.setJoiningDate(LocalDate.now());
        user.setActive(true);

        // Set Manager
        if (body.get("managerId") != null) {
            Long managerId = Long.valueOf(body.get("managerId").toString());
            userRepo.findById(managerId).ifPresent(user::setManager);
        }

        user = userRepo.save(user);

        // Create Leave Balance
        int year = LocalDate.now().getYear();

        LeaveBalance balance = new LeaveBalance();
        balance.setUser(user);
        balance.setAnnualBalance(annualDays);
        balance.setSickBalance(sickDays);
        balance.setCasualBalance(casualDays);
        balance.setAnnualUsed(0);
        balance.setSickUsed(0);
        balance.setCasualUsed(0);
        balance.setLastCreditedDate(LocalDate.now());
        balance.setYear(year);

        balanceRepo.save(balance);

        return ResponseEntity.ok(user);
    }

    // ===== UPDATE USER =====
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody Map<String, Object> body) {

        User user = userRepo.findById(id).orElseThrow();

        if (body.containsKey("email"))
            user.setEmail((String) body.get("email"));

        if (body.containsKey("firstName"))
            user.setFirstName((String) body.get("firstName"));

        if (body.containsKey("lastName"))
            user.setLastName((String) body.get("lastName")); // FIXED

        if (body.containsKey("role"))
            user.setRole(User.Role.valueOf((String) body.get("role"))); // FIXED

        if (body.containsKey("department"))
            user.setDepartment(User.Department.valueOf((String) body.get("department")));

        if (body.containsKey("designation"))
            user.setDesignation((String) body.get("designation"));

        if (body.containsKey("active"))
            user.setActive((Boolean) body.get("active"));

        if (body.containsKey("managerId")) {
            Long managerId = Long.valueOf(body.get("managerId").toString());
            userRepo.findById(managerId).ifPresent(user::setManager);
        }

        return ResponseEntity.ok(userRepo.save(user));
    }

    // ===== DELETE USER =====
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
