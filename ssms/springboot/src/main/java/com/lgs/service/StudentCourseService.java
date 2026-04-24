package com.lgs.service;

import com.lgs.entity.StudentCourse;

import java.util.List;

public interface StudentCourseService {
    List<StudentCourse> selectAll(StudentCourse studentCourse);
    void add(StudentCourse studentCourse);
    void updateScore(StudentCourse studentCourse);
    void deleteById(Integer id);
    void deleteByStudentIdAndCourseId(StudentCourse studentCourse);
    List<StudentCourse> selectByCourseId(Integer course_id);
    List<StudentCourse> selectByStudentId(String student_id);
    List<StudentCourse> selectByTeachingClassId(Integer teaching_class_id);
    StudentCourse selectByStudentIdAndCourseId(String student_id, Integer course_id);
    double calculateTotalScore(double usualScore, double midtermScore, double finalScore, Integer usualWeight, Integer midtermWeight, Integer finalWeight);
}