package com.lgs.service;

import com.lgs.entity.CourseEvaluation;

import java.util.List;

public interface CourseEvaluationService {
    // 提交或更新评价
    boolean saveOrUpdate(CourseEvaluation courseEvaluation);
    
    // 查询所有评价
    List<CourseEvaluation> getAll();
    
    // 删除评价
    boolean deleteById(Integer id);
    
    // 根据学生ID和课程ID查询评价
    CourseEvaluation getByStudentAndCourse(String studentId, Integer courseId);
    
    // 根据课程ID查询评价列表
    List<CourseEvaluation> getByCourseId(Integer courseId);
    
    // 根据教师ID查询评价列表
    List<CourseEvaluation> getByTeacherId(String teacherId);
    
    // 统计评价数量
    int count();
}
