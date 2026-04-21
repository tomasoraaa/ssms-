package com.lgs.mapper;

import com.lgs.entity.CourseSelection;

import java.util.List;

public interface CourseSelectionMapper {
    List<CourseSelection> selectAll(CourseSelection courseSelection);
    void insert(CourseSelection courseSelection);
    void updateById(CourseSelection courseSelection);
    void deleteById(Integer id);
}