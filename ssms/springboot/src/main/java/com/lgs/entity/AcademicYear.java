package com.lgs.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AcademicYear {
    private Integer id;
    private String year;
    private Integer semester;
    private LocalDate start_date;
    private LocalDate end_date;
    private LocalDateTime selection_start;
    private LocalDateTime selection_end;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public LocalDateTime getSelection_start() {
        return selection_start;
    }

    public void setSelection_start(LocalDateTime selection_start) {
        this.selection_start = selection_start;
    }

    public LocalDateTime getSelection_end() {
        return selection_end;
    }

    public void setSelection_end(LocalDateTime selection_end) {
        this.selection_end = selection_end;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}