package com.lgs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ScoreDetail {
    private Integer id;
    private String student_id;
    private Integer course_id;
    private Integer teaching_class_id;
    private Double usual_score;
    private Double midterm_score;
    private Double final_score;
    private Double total_score;
    private Date created_at;
    private Date updated_at;
}