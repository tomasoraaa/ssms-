package com.lgs.service;

import com.github.pagehelper.PageInfo;
import com.lgs.entity.AcademicYear;
import java.util.List;

public interface AcademicYearService {
    void add(AcademicYear academicYear);
    void updateById(AcademicYear academicYear);
    void deleteById(Integer id);
    AcademicYear selectById(Integer id);
    PageInfo<AcademicYear> selectPage(AcademicYear academicYear, Integer pageNum, Integer pageSize);
    List<AcademicYear> selectAll(AcademicYear academicYear);
    AcademicYear selectCurrent();
}
