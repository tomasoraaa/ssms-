package com.lgs.mapper;

import com.lgs.entity.StudentCourse;

import java.util.List;

public interface StudentCourseMapper {
    List<StudentCourse> selectAll(StudentCourse studentCourse);
    void insert(StudentCourse studentCourse);
    void updateScore(StudentCourse studentCourse);
    void deleteById(Integer id);
    void deleteByStudentIdAndCourseId(StudentCourse studentCourse);
    List<StudentCourse> selectByCourseId(String courseId);
    List<StudentCourse> selectByStudentId(String studentId);
}