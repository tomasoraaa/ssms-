package com.lgs.entity;

import lombok.Data;

@Data
public class StudentCourse {
    private Integer id;
    private String student_id;
    private String course_id;
    private Integer teaching_class_id;
    private Integer academic_year_id;
    private String teacher_id;
    private String teacher_name;
    private Integer status;
    private Double score;
    private Integer is_makeup;
    private Double original_score;

}