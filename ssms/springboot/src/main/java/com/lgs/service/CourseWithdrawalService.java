package com.lgs.service;

import com.lgs.entity.CourseWithdrawal;

import java.util.List;

public interface CourseWithdrawalService {
    void add(CourseWithdrawal courseWithdrawal);
    List<CourseWithdrawal> selectAll(CourseWithdrawal courseWithdrawal);
    List<CourseWithdrawal> selectByStudentId(String studentId);
    void updateById(CourseWithdrawal courseWithdrawal);
    void deleteById(Integer id);
    void approve(Integer id);
    void reject(Integer id);
}