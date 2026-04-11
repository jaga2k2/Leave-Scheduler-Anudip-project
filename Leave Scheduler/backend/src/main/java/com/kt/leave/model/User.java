package com.kt.leave.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Department department;

    private String designation;
    private LocalDate joiningDate;
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnoreProperties({"password", "manager", "leaveApplications", "leaveBalance"})
    private User manager;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LeaveApplication> leaveApplications;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private LeaveBalance leaveBalance;

    // ===================== CONSTRUCTORS =====================

    public User() {}

    public User(Long id, String username, String password, String email,
                String firstName, String lastName, Role role,
                Department department, String designation,
                LocalDate joiningDate, boolean active,
                User manager, List<LeaveApplication> leaveApplications,
                LeaveBalance leaveBalance) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.active = active;
        this.manager = manager;
        this.leaveApplications = leaveApplications;
        this.leaveBalance = leaveBalance;
    }

    // ===================== GETTERS =====================

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Role getRole() { return role; }
    public Department getDepartment() { return department; }
    public String getDesignation() { return designation; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public boolean isActive() { return active; }
    public User getManager() { return manager; }
    public List<LeaveApplication> getLeaveApplications() { return leaveApplications; }
    public LeaveBalance getLeaveBalance() { return leaveBalance; }

    // ===================== SETTERS =====================

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRole(Role role) { this.role = role; }
    public void setDepartment(Department department) { this.department = department; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public void setActive(boolean active) { this.active = active; }
    public void setManager(User manager) { this.manager = manager; }
    public void setLeaveApplications(List<LeaveApplication> leaveApplications) { this.leaveApplications = leaveApplications; }
    public void setLeaveBalance(LeaveBalance leaveBalance) { this.leaveBalance = leaveBalance; }

    // ===================== ENUMS =====================

    public enum Role {
        EMPLOYEE, MANAGER, BUSINESS_MANAGER, MANAGING_DIRECTOR, ADMIN
    }

    public enum Department {
        ENGINEERING, MANAGEMENT, HR, FINANCE, OPERATIONS
    }
}
