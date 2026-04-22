package com.lgs.service;

import com.lgs.entity.StudentCourse;

import java.util.List;

public interface StudentCourseService {
    List<StudentCourse> selectAll(StudentCourse studentCourse);
    void add(StudentCourse studentCourse);
    void updateScore(StudentCourse studentCourse);
    void deleteById(Integer id);
    void deleteByStudentIdAndCourseId(StudentCourse studentCourse);
    List<StudentCourse> selectByCourseId(String courseId);
    List<StudentCourse> selectByStudentId(String studentId);
    List<StudentCourse> selectByTeachingClassId(Integer teachingClassId);
}