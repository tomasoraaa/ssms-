package com.lgs.mapper;

import com.lgs.entity.CourseTeacher;
import java.util.List;

public interface CourseTeacherMapper {
    void insert(CourseTeacher courseTeacher);
    List<CourseTeacher> selectByCourseId(Integer course_id);
    List<CourseTeacher> selectByTeacherId(String teacher_id);
    List<CourseTeacher> selectByTeachingClassId(Integer teaching_class_id);
    void deleteByCourseIdAndTeacherId(Integer course_id, String teacher_id);
    void deleteByTeachingClassId(Integer teaching_class_id);
    void updateById(CourseTeacher courseTeacher);
}
