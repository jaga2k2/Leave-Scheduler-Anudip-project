package com.kt.leave.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "leave_balances")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private int annualBalance;
    private int sickBalance;
    private int casualBalance;

    private int annualUsed;
    private int sickUsed;
    private int casualUsed;

    private LocalDate lastCreditedDate;
    private int year;

    // ===================== CONSTRUCTORS =====================

    public LeaveBalance() {
    }

    public LeaveBalance(Long id, User user, int annualBalance, int sickBalance, int casualBalance,
                        int annualUsed, int sickUsed, int casualUsed,
                        LocalDate lastCreditedDate, int year) {
        this.id = id;
        this.user = user;
        this.annualBalance = annualBalance;
        this.sickBalance = sickBalance;
        this.casualBalance = casualBalance;
        this.annualUsed = annualUsed;
        this.sickUsed = sickUsed;
        this.casualUsed = casualUsed;
        this.lastCreditedDate = lastCreditedDate;
        this.year = year;
    }

    // ===================== GETTERS =====================

    public Long getId() { return id; }
    public User getUser() { return user; }
    public int getAnnualBalance() { return annualBalance; }
    public int getSickBalance() { return sickBalance; }
    public int getCasualBalance() { return casualBalance; }
    public int getAnnualUsed() { return annualUsed; }
    public int getSickUsed() { return sickUsed; }
    public int getCasualUsed() { return casualUsed; }
    public LocalDate getLastCreditedDate() { return lastCreditedDate; }
    public int getYear() { return year; }

    // ===================== SETTERS =====================

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setAnnualBalance(int annualBalance) { this.annualBalance = annualBalance; }
    public void setSickBalance(int sickBalance) { this.sickBalance = sickBalance; }
    public void setCasualBalance(int casualBalance) { this.casualBalance = casualBalance; }
    public void setAnnualUsed(int annualUsed) { this.annualUsed = annualUsed; }
    public void setSickUsed(int sickUsed) { this.sickUsed = sickUsed; }
    public void setCasualUsed(int casualUsed) { this.casualUsed = casualUsed; }
    public void setLastCreditedDate(LocalDate lastCreditedDate) { this.lastCreditedDate = lastCreditedDate; }
    public void setYear(int year) { this.year = year; }
}
