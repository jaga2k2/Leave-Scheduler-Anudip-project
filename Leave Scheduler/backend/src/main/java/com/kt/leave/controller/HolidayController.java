package com.kt.leave.controller;

import com.kt.leave.model.Holiday;
import com.kt.leave.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    @Autowired private HolidayRepository holidayRepo;

    @GetMapping
    public ResponseEntity<List<Holiday>> getAll() {
        int year = LocalDate.now().getYear();
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate to = LocalDate.of(year, 12, 31);
        return ResponseEntity.ok(holidayRepo.findByDateBetweenOrderByDate(from, to));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Holiday> create(@RequestBody Holiday holiday) {
        return ResponseEntity.ok(holidayRepo.save(holiday));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        holidayRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
