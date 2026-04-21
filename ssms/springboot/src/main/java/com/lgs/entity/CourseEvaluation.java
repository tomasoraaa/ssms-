package com.lgs.entity;

import java.util.Date;

public class CourseEvaluation {
    private Integer id;
    private String student_id;
    private String student_name;
    private Integer course_id;
    private String course_name;
    private String teacher_id;
    private Integer rating;
    private String content;
    private String teacher_evaluation;
    private Date evaluation_time;

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

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
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

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeacher_evaluation() {
        return teacher_evaluation;
    }

    public void setTeacher_evaluation(String teacher_evaluation) {
        this.teacher_evaluation = teacher_evaluation;
    }

    public Date getEvaluation_time() {
        return evaluation_time;
    }

    public void setEvaluation_time(Date evaluation_time) {
        this.evaluation_time = evaluation_time;
    }
}