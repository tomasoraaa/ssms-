package com.lgs.entity;

public class StudentCourse {
    private Integer id;
    private String studentId;
    private String courseId;
    private Integer teachingClassId; // 教学班ID
    private Integer academicYearId; // 学年学期ID
    private String teacherId;
    private String teacherName;
    private Integer status;
    private Double score;
    private Integer isMakeup; // 是否补考：0-否，1-是
    private Double originalScore; // 原始成绩（补考前）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(Integer teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public Integer getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Integer academicYearId) {
        this.academicYearId = academicYearId;
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

    public Integer getIsMakeup() {
        return isMakeup;
    }

    public void setIsMakeup(Integer isMakeup) {
        this.isMakeup = isMakeup;
    }

    public Double getOriginalScore() {
        return originalScore;
    }

    public void setOriginalScore(Double originalScore) {
        this.originalScore = originalScore;
    }
}
