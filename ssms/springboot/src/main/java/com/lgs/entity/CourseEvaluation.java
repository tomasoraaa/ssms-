package com.lgs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CourseEvaluation {
    private Integer id;
    private String student_id;
    private String student_name;
    private Integer course_id;
    private String course_name;
    private String teacher_id;
    private Integer rating;
    private String content;
    private String teacher_evaluation;
    private Date evaluation_time;

}