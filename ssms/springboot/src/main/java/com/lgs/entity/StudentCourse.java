package com.lgs.entity;

public class StudentCourse {
    private Integer id;
    private String student_id;
    private String course_id;
    private Integer teaching_class_id;
    private Integer academic_year_id;
    private String teacher_id;
    private String teacher_name;
    private Integer status;
    private Double score;
    private Integer is_makeup;
    private Double original_score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public Integer getTeaching_class_id() {
        return teaching_class_id;
    }

    public void setTeaching_class_id(Integer teaching_class_id) {
        this.teaching_class_id = teaching_class_id;
    }

    public Integer getAcademic_year_id() {
        return academic_year_id;
    }

    public void setAcademic_year_id(Integer academic_year_id) {
        this.academic_year_id = academic_year_id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getIs_makeup() {
        return is_makeup;
    }

    public void setIs_makeup(Integer is_makeup) {
        this.is_makeup = is_makeup;
    }

    public Double getOriginal_score() {
        return original_score;
    }

    public void setOriginal_score(Double original_score) {
        this.original_score = original_score;
    }
}