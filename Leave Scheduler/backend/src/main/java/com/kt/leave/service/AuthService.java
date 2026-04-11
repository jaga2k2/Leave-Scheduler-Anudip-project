package com.kt.leave.service;

import com.kt.leave.dto.AuthDtos.*;
import com.kt.leave.dto.CreateUserRequest;
import com.kt.leave.model.LeaveBalance;
import com.kt.leave.model.User;
import com.kt.leave.repository.LeaveBalanceRepository;
import com.kt.leave.repository.UserRepository;
import com.kt.leave.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authManager;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepo;
    @Autowired private LeaveBalanceRepository balanceRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${app.leave.annual-days:21}")
    private int annualDays;
    @Value("${app.leave.sick-days:10}")
    private int sickDays;
    @Value("${app.leave.casual-days:7}")
    private int casualDays;
    @Value("${app.bootstrap.admin-key:changeme-admin-key}")
    private String bootstrapAdminKey;

    public LoginResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
        User user = userRepo.findByUsername(req.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(userDetails);

        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUsername(user.getUsername());
        resp.setRole(user.getRole().name());
        resp.setUserId(user.getId());
        resp.setFullName(user.getFirstName() + " " + user.getLastName());
        return resp;
    }

    public LoginResponse register(CreateUserRequest req) {
        return createAndLogin(req, User.Role.EMPLOYEE);
    }

    public LoginResponse bootstrapAdmin(CreateUserRequest req, String setupKey) {
        if (setupKey == null || !setupKey.equals(bootstrapAdminKey)) {
            throw new RuntimeException("Invalid admin setup key");
        }
        return createAndLogin(req, User.Role.ADMIN);
    }

    public void changePassword(String username, ChangePasswordRequest req) {
        User user = userRepo.findByUsername(username).orElseThrow();
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect current password");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);
    }

    private void validateRegistration(CreateUserRequest req) {
        if (req.getUsername() == null || req.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (req.getFirstName() == null || req.getFirstName().trim().isEmpty()) {
            throw new RuntimeException("First name is required");
        }
        if (req.getLastName() == null || req.getLastName().trim().isEmpty()) {
            throw new RuntimeException("Last name is required");
        }
        if (userRepo.existsByUsername(req.getUsername().trim())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(req.getEmail().trim())) {
            throw new RuntimeException("Email already exists");
        }
    }

    private LoginResponse createAndLogin(CreateUserRequest req, User.Role role) {
        validateRegistration(req);

        User user = new User();
        user.setUsername(req.getUsername().trim());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail().trim());
        user.setFirstName(req.getFirstName().trim());
        user.setLastName(req.getLastName().trim());
        user.setRole(role);
        user.setDepartment(parseDepartment(req.getDepartment()));
        user.setDesignation(req.getDesignation() != null ? req.getDesignation().trim() : null);
        user.setJoiningDate(LocalDate.now());
        user.setActive(true);
        user = userRepo.save(user);

        LeaveBalance balance = new LeaveBalance();
        balance.setUser(user);
        balance.setAnnualBalance(annualDays);
        balance.setSickBalance(sickDays);
        balance.setCasualBalance(casualDays);
        balance.setAnnualUsed(0);
        balance.setSickUsed(0);
        balance.setCasualUsed(0);
        balance.setLastCreditedDate(LocalDate.now());
        balance.setYear(LocalDate.now().getYear());
        balanceRepo.save(balance);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(req.getUsername().trim());
        loginRequest.setPassword(req.getPassword());
        return login(loginRequest);
    }

    private User.Department parseDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            return User.Department.ENGINEERING;
        }
        return User.Department.valueOf(department.trim().toUpperCase());
    }
}
