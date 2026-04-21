package com.lgs.service;

import com.github.pagehelper.PageInfo;
import com.lgs.entity.Course;

public interface CourseService {
    void add(Course course);
    PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize);
    void updateById(Course course);
    void deleteById(Integer id);
    Course selectById(Integer id);

    java.util.List<Course> selectByStudentId(String studentId);
    
    java.util.List<Course> selectByTeacherId(String teacherId);
    
    /**
     * 统计课程数量
     */
    int count();
}