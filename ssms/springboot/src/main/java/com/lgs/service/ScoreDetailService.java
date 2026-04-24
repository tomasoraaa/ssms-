package com.lgs.service;

import com.lgs.entity.ScoreDetail;

import java.util.List;

public interface ScoreDetailService {
    void add(ScoreDetail scoreDetail);
    void updateById(ScoreDetail scoreDetail);
    void deleteById(Integer id);
    ScoreDetail selectById(Integer id);
    List<ScoreDetail> selectAll(ScoreDetail scoreDetail);
    ScoreDetail selectByStudentAndCourse(String student_id, Integer course_id);
    ScoreDetail selectByStudentAndTeachingClass(String student_id, Integer teaching_class_id);
    List<ScoreDetail> selectByCourseId(Integer course_id);
    List<ScoreDetail> selectByTeachingClassId(Integer teaching_class_id);
    List<ScoreDetail> selectByStudentId(String student_id);
    List<ScoreDetail> getWarningStudents(Integer teaching_class_id);
    double calculateTotalScore(double usual_score, double midterm_score, double final_score, Integer usual_weight, Integer midterm_weight, Integer final_weight);
}