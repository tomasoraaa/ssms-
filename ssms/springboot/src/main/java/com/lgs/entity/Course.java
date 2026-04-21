package com.lgs.entity;

import java.util.List;

public class Course {
    private Integer id;
    private String course_name;
    private String course_code;
    private Integer credit;
    private String description;
    private String teacher_name;
    private String teacher_id;
    private Double score;
    private String academic_year_name; // 修读学期
    private List<TeachingClass> teachingClasses; // 关联的教学班列表

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getAcademic_year_name() {
        return academic_year_name;
    }

    public void setAcademic_year_name(String academic_year_name) {
        this.academic_year_name = academic_year_name;
    }

    public List<TeachingClass> getTeachingClasses() {
        return teachingClasses;
    }

    public void setTeachingClasses(List<TeachingClass> teachingClasses) {
        this.teachingClasses = teachingClasses;
    }
}
