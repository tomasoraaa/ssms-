package com.lgs.mapper;

import com.lgs.entity.Course;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper {
    List<Course> selectAll(Course course);
    void insert(Course course);
    void updateById(Course course);
    void deleteById(Integer id);
    Course selectById(Integer id);
    Course selectByCourseCode(String course_code);
    
    /**
     * 查询课程及其教师信息
     */
    Course selectWithTeachers(Integer id);
    
    /**
     * 统计课程数量
     */
    @Select("select count(*) from course")
    int count();
}