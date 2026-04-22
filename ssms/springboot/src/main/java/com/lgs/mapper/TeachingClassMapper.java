package com.lgs.mapper;

import com.lgs.entity.TeachingClass;
import java.util.List;

public interface TeachingClassMapper {
    void insert(TeachingClass teachingClass);
    void updateById(TeachingClass teachingClass);
    void deleteById(Integer id);
    TeachingClass selectById(Integer id);
    List<TeachingClass> selectAll(TeachingClass teachingClass);
    List<TeachingClass> selectByAcademicYearId(Integer academic_year_id);
    List<TeachingClass> selectAvailable(Integer academic_year_id);
    TeachingClass selectByClassCode(String class_code);
}
