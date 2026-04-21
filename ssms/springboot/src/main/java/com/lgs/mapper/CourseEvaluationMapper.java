package com.lgs.mapper;

import com.lgs.entity.CourseEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseEvaluationMapper {
    // 添加评价
    int insert(CourseEvaluation courseEvaluation);
    
    // 更新评价
    int update(CourseEvaluation courseEvaluation);
    
    // 删除评价
    int deleteById(Integer id);
    
    // 查询所有评价
    List<CourseEvaluation> selectAll();
    
    // 根据学生ID和课程ID查询评价
    CourseEvaluation selectByStudentAndCourse(@Param("studentId") String studentId, @Param("courseId") Integer courseId);
    
    // 根据课程ID查询评价列表
    List<CourseEvaluation> selectByCourseId(Integer courseId);
    
    // 根据教师ID查询评价列表
    List<CourseEvaluation> selectByTeacherId(String teacherId);
    
    // 统计评价数量
    @Select("select count(*) from course_evaluation")
    int count();
}
