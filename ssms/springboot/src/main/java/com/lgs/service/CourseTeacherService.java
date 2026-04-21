package com.lgs.service;

import com.lgs.entity.CourseTeacher;
import java.util.List;

public interface CourseTeacherService {
    void add(CourseTeacher courseTeacher);
    List<CourseTeacher> selectByCourseId(Integer courseId);
    List<CourseTeacher> selectByTeacherId(String teacherId);
    List<CourseTeacher> selectByTeachingClassId(Integer teachingClassId);
    void deleteByCourseIdAndTeacherId(Integer courseId, String teacherId);
    void deleteByTeachingClassId(Integer teachingClassId);
    void updateById(CourseTeacher courseTeacher);
}
