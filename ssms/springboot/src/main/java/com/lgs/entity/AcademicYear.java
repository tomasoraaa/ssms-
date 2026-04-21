package com.lgs.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AcademicYear {
    private Integer id;
    private String year;
    private Integer semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime selectionStart;
    private LocalDateTime selectionEnd;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(LocalDateTime selectionStart) {
        this.selectionStart = selectionStart;
    }

    public LocalDateTime getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionEnd(LocalDateTime selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
