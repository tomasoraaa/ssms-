package com.lgs.mapper;

import com.lgs.entity.ScoreDetail;

import java.util.List;

public interface ScoreDetailMapper {
    List<ScoreDetail> selectAll(ScoreDetail scoreDetail);
    void insert(ScoreDetail scoreDetail);
    void updateById(ScoreDetail scoreDetail);
    void deleteById(Integer id);
    ScoreDetail selectById(Integer id);
    ScoreDetail selectByStudentAndCourse(String student_id, Integer course_id);
    ScoreDetail selectByStudentAndTeachingClass(String student_id, Integer teaching_class_id);
    List<ScoreDetail> selectByCourseId(Integer course_id);
    List<ScoreDetail> selectByTeachingClassId(Integer teaching_class_id);
    List<ScoreDetail> selectByStudentId(String student_id);
    List<ScoreDetail> selectByStudentIdWithParams(String studentId, Integer courseId, Integer academicYearId);
}