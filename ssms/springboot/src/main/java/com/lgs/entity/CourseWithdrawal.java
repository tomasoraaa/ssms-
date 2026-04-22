package com.lgs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CourseWithdrawal {
    private Integer id;
    private String student_id;
    private String course_id;
    private String teacher_id;
    private String teacher_name;
    private Date withdrawal_time;
    private String reason;
    private Integer status;

}