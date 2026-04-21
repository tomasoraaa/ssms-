package com.lgs.entity;

public class TeachingClass {
    private Integer id;
    private String class_code;
    private Integer course_id;
    private String course_name;
    private String course_code;
    private Integer academic_year_id;
    private String academic_year_name;
    private Integer capacity;
    private Integer selected_count;
    private String location;
    private String schedule;
    private Integer status;
    private String teacher_name;
    private String teacher_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public Integer getAcademic_year_id() {
        return academic_year_id;
    }

    public void setAcademic_year_id(Integer academic_year_id) {
        this.academic_year_id = academic_year_id;
    }

    public String getAcademic_year_name() {
        return academic_year_name;
    }

    public void setAcademic_year_name(String academic_year_name) {
        this.academic_year_name = academic_year_name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getSelected_count() {
        return selected_count;
    }

    public void setSelected_count(Integer selected_count) {
        this.selected_count = selected_count;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }
}
