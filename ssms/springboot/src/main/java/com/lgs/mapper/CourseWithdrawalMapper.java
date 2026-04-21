package com.lgs.mapper;

import com.lgs.entity.CourseWithdrawal;

import java.util.List;

public interface CourseWithdrawalMapper {
    void insert(CourseWithdrawal courseWithdrawal);
    List<CourseWithdrawal> selectAll(CourseWithdrawal courseWithdrawal);
    void updateById(CourseWithdrawal courseWithdrawal);
    void deleteById(Integer id);
    List<CourseWithdrawal> selectByStudentId(String studentId);
}