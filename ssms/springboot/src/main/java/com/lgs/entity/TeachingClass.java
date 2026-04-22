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
    private Integer day_of_week;
    private Integer period_start;
    private Integer period_end;
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

    public Integer getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(Integer day_of_week) {
        this.day_of_week = day_of_week;
    }

    public Integer getPeriod_start() {
        return period_start;
    }

    public void setPeriod_start(Integer period_start) {
        this.period_start = period_start;
    }

    public Integer getPeriod_end() {
        return period_end;
    }

    public void setPeriod_end(Integer period_end) {
        this.period_end = period_end;
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

    public String getScheduleText() {
        if (day_of_week == null || period_start == null || period_end == null) {
            return "";
        }
        String[] days = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        String day = day_of_week >= 1 && day_of_week <= 7 ? days[day_of_week] : "第" + day_of_week + "天";
        return day + " 第" + period_start + "-" + period_end + "节";
    }

    public boolean hasTimeConflict(TeachingClass other) {
        if (this.day_of_week == null || other.day_of_week == null ||
            this.period_start == null || other.period_start == null ||
            this.period_end == null || other.period_end == null) {
            return false;
        }
        if (!this.day_of_week.equals(other.day_of_week)) {
            return false;
        }
        return this.period_start <= other.period_end && other.period_start <= this.period_end;
    }
}
