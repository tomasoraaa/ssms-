package com.lgs.entity;

import lombok.Data;

@Data
public class CourseTeacher {
    private Integer id;
    private Integer course_id;
    private String teacher_id;
    private String teacher_name;
    private Integer teaching_class_id;
    private Integer is_main_teacher;

}
