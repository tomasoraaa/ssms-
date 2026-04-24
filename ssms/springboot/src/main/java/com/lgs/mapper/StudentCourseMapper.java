package com.lgs.mapper;

import com.lgs.entity.StudentCourse;

import java.util.List;

public interface StudentCourseMapper {
    List<StudentCourse> selectAll(StudentCourse studentCourse);
    void insert(StudentCourse studentCourse);
    void updateScore(StudentCourse studentCourse);
    void deleteById(Integer id);
    void deleteByStudentIdAndCourseId(StudentCourse studentCourse);
    List<StudentCourse> selectByCourseId(Integer course_id);
    List<StudentCourse> selectByStudentId(String student_id);
    List<StudentCourse> selectByTeachingClassId(Integer teaching_class_id);
    StudentCourse selectByStudentIdAndCourseId(String student_id, Integer course_id);
}