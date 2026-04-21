package com.lgs.service;

import com.github.pagehelper.PageInfo;
import com.lgs.entity.TeachingClass;
import java.util.List;

public interface TeachingClassService {
    void add(TeachingClass teachingClass);
    void updateById(TeachingClass teachingClass);
    void deleteById(Integer id);
    TeachingClass selectById(Integer id);
    PageInfo<TeachingClass> selectPage(TeachingClass teachingClass, Integer pageNum, Integer pageSize);
    List<TeachingClass> selectAll(TeachingClass teachingClass);
    List<TeachingClass> selectByAcademicYearId(Integer academic_year_id);
    List<TeachingClass> selectAvailable(Integer academic_year_id);
    List<TeachingClass> selectByCourseId(Integer course_id);
}
