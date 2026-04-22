package com.lgs.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AcademicYear {
    private Integer id;
    private String year;
    private Integer semester;
    private LocalDate start_date;
    private LocalDate end_date;
    private LocalDateTime selection_start;
    private LocalDateTime selection_end;
    private Integer status;

}