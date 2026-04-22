package com.lgs.entity;

import lombok.Data;

import java.util.List;

@Data
public class Course {
    private Integer id;
    private String course_name;
    private String course_code;
    private Integer credit;
    private String description;
    private String teacher_name;
    private String teacher_id;
    private Double score;
    private Integer teaching_class_id; // 教学班ID
    private String academic_year_name; // 修读学期
    private List<TeachingClass> teachingClasses; // 关联的教学班列表

}
