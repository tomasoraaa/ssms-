package com.lgs.mapper;

import com.lgs.entity.CourseEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseEvaluationMapper {
    int insert(CourseEvaluation courseEvaluation);

    int update(CourseEvaluation courseEvaluation);

    int deleteById(Integer id);

    List<CourseEvaluation> selectAll();

    CourseEvaluation selectByStudentAndCourse(@Param("student_id") String student_id, @Param("course_id") Integer course_id);

    List<CourseEvaluation> selectByCourseId(Integer course_id);

    List<CourseEvaluation> selectByTeacherId(String teacher_id);

    @Select("select count(*) from course_evaluation")
    int count();
}