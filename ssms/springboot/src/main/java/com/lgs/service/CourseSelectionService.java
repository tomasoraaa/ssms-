package com.lgs.service;

import com.lgs.entity.CourseSelection;

import java.util.List;

public interface CourseSelectionService {
    void add(CourseSelection courseSelection);
    List<CourseSelection> selectAll(CourseSelection courseSelection);
    void updateById(CourseSelection courseSelection);
    void deleteById(Integer id);
    void approve(Integer id);
    void reject(Integer id);
}