package com.lgs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AcademicYear {
    private Integer id;
    private String year;
    private Integer semester;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime selection_start;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime selection_end;
    private Integer status;

}