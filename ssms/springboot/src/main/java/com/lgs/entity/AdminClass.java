package com.lgs.entity;

public class AdminClass {
    private Integer id;
    private String grade;
    private String major;
    private String class_name;
    private String class_code;
    private String counselor_id;
    private String counselor_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_code() {
        return class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public String getCounselor_id() {
        return counselor_id;
    }

    public void setCounselor_id(String counselor_id) {
        this.counselor_id = counselor_id;
    }

    public String getCounselor_name() {
        return counselor_name;
    }

    public void setCounselor_name(String counselor_name) {
        this.counselor_name = counselor_name;
    }
}