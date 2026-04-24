package com.lgs.mapper;

import com.lgs.entity.MakeupExam;

import java.util.List;

public interface MakeupExamMapper {
    List<MakeupExam> selectAll(MakeupExam makeup_exam);
    void insert(MakeupExam makeup_exam);
    void updateById(MakeupExam makeup_exam);
    void deleteById(Integer id);
    MakeupExam selectById(Integer id);
    List<MakeupExam> selectByStudentId(String student_id);
    List<MakeupExam> selectByCourseId(Integer course_id);
    List<MakeupExam> selectByTeachingClassId(Integer teaching_class_id);
}