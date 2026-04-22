package com.lgs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CourseSelection {
    private Integer id;
    private String user_id;
    private String user_name;
    private String user_type;
    private String course_id;
    private String course_name;
    private String teacher_id;
    private String teacher_name;
    private Integer teaching_class_id;
    private String teaching_class_code;
    private Integer status; // 0: 待审核, 1: 已通过, 2: 已拒绝
    private Date create_time;

}
