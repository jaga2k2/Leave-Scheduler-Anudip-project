package com.kt.leave.repository;

import com.kt.leave.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByUserId(Long userId);
    Optional<LeaveBalance> findByUserIdAndYear(Long userId, int year);
}
