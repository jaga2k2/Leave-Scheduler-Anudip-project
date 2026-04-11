package com.kt.leave.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    private String description;

    @Enumerated(EnumType.STRING)
    private HolidayType type;

    public enum HolidayType {
        NATIONAL, OPTIONAL, COMPANY
    }

    // ===================== CONSTRUCTORS =====================

    public Holiday() {
    }

    public Holiday(Long id, String name, LocalDate date, String description, HolidayType type) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.type = type;
    }

    // ===================== GETTERS =====================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public HolidayType getType() {
        return type;
    }

    // ===================== SETTERS =====================

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(HolidayType type) {
        this.type = type;
    }

    // ===================== BUILDER =====================

    public static HolidayBuilder builder() {
        return new HolidayBuilder();
    }

    public static class HolidayBuilder {
        private Long id;
        private String name;
        private LocalDate date;
        private String description;
        private HolidayType type;

        public HolidayBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HolidayBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HolidayBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public HolidayBuilder description(String description) {
            this.description = description;
            return this;
        }

        public HolidayBuilder type(HolidayType type) {
            this.type = type;
            return this;
        }

        public Holiday build() {
            return new Holiday(id, name, date, description, type);
        }
    }
}