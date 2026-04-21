package com.lgs.service;

import com.lgs.entity.StudentCourse;

import java.util.List;

public interface StudentCourseService {
    void add(StudentCourse studentCourse);
    List<StudentCourse> selectAll(StudentCourse studentCourse);
    void updateScore(StudentCourse studentCourse);
    void deleteById(Integer id);
    void deleteByStudentIdAndCourseId(StudentCourse studentCourse);
    List<StudentCourse> selectByCourseId(String courseId);
    List<StudentCourse> selectByStudentId(String studentId);
}