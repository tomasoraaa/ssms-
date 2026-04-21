package com.lgs.entity;

public class CourseTeacher {
    private Integer id;
    private Integer course_id;
    private String teacher_id;
    private String teacher_name;
    private Integer teaching_class_id;
    private Integer is_main_teacher;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public Integer getTeaching_class_id() {
        return teaching_class_id;
    }

    public void setTeaching_class_id(Integer teaching_class_id) {
        this.teaching_class_id = teaching_class_id;
    }

    public Integer getIs_main_teacher() {
        return is_main_teacher;
    }

    public void setIs_main_teacher(Integer is_main_teacher) {
        this.is_main_teacher = is_main_teacher;
    }
}
