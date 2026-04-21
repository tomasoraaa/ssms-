package com.lgs.entity;

public class CourseTeacher {
    private Integer id;
    private Integer courseId;
    private String teacherId;
    private String teacherName;
    private Integer teachingClassId;
    private Integer isMainTeacher;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(Integer teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public Integer getIsMainTeacher() {
        return isMainTeacher;
    }

    public void setIsMainTeacher(Integer isMainTeacher) {
        this.isMainTeacher = isMainTeacher;
    }
}
