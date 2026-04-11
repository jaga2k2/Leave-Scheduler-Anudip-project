package com.kt.leave.repository;

import com.kt.leave.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByDateBetweenOrderByDate(LocalDate from, LocalDate to);
    @Query("SELECT h FROM Holiday h WHERE year(h.date) = :year ORDER BY h.date")
    List<Holiday> findByDateYear(int year);
    boolean existsByDate(LocalDate date);
}
