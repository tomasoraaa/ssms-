package com.lgs.mapper;

import com.lgs.entity.AcademicYear;
import java.util.List;

public interface AcademicYearMapper {
    void insert(AcademicYear academicYear);
    void updateById(AcademicYear academicYear);
    void deleteById(Integer id);
    AcademicYear selectById(Integer id);
    List<AcademicYear> selectAll(AcademicYear academicYear);
    AcademicYear selectCurrent();
}
