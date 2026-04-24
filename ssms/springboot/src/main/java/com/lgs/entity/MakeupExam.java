package com.lgs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MakeupExam {
    private Integer id;
    private String student_id;
    private Integer course_id;
    private Integer teaching_class_id;
    private String exam_type;
    private Double makeup_score;
    private Double original_score;
    private Integer is_final;
    private String status;
    private String reason;
    private Date created_at;
    private Date updated_at;
}