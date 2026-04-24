package com.lgs.service;

import com.lgs.entity.MakeupExam;

import java.util.List;

public interface MakeupExamService {
    void add(MakeupExam makeupExam);
    void updateById(MakeupExam makeupExam);
    void deleteById(Integer id);
    MakeupExam selectById(Integer id);
    List<MakeupExam> selectAll(MakeupExam makeupExam);
    List<MakeupExam> selectByStudentId(String student_id);
    List<MakeupExam> selectByCourseId(Integer course_id);
    List<MakeupExam> selectByTeachingClassId(Integer teaching_class_id);
    void approve(Integer id);
    void reject(Integer id);
}