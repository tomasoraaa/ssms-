package com.lgs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CourseWithdrawal {
    private Integer id;
    private String student_id;
    private String student_name;
    private String course_id;
    private String course_name;
    private String course_code;
    private String class_code;
    private String teacher_id;
    private String teacher_name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date withdrawal_time;
    private String reason;
    private Integer status;

}