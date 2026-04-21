package com.lgs.mapper;

import com.lgs.entity.CourseTeacher;
import java.util.List;

public interface CourseTeacherMapper {
    void insert(CourseTeacher courseTeacher);
    List<CourseTeacher> selectByCourseId(Integer courseId);
    List<CourseTeacher> selectByTeacherId(String teacherId);
    List<CourseTeacher> selectByTeachingClassId(Integer teachingClassId);
    void deleteByCourseIdAndTeacherId(Integer courseId, String teacherId);
    void deleteByTeachingClassId(Integer teachingClassId);
    void updateById(CourseTeacher courseTeacher);
}
