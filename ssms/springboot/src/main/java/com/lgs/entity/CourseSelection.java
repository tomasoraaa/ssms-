package com.lgs.entity;

import java.util.Date;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getTeaching_class_id() {
        return teaching_class_id;
    }

    public void setTeaching_class_id(Integer teaching_class_id) {
        this.teaching_class_id = teaching_class_id;
    }

    public String getTeaching_class_code() {
        return teaching_class_code;
    }

    public void setTeaching_class_code(String teaching_class_code) {
        this.teaching_class_code = teaching_class_code;
    }
}
