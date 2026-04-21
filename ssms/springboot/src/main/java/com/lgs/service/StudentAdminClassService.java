package com.lgs.service;

import com.github.pagehelper.PageInfo;
import com.lgs.entity.StudentAdminClass;
import java.util.List;

public interface StudentAdminClassService {
    void add(StudentAdminClass studentAdminClass);
    void updateById(StudentAdminClass studentAdminClass);
    void deleteById(Integer id);
    StudentAdminClass selectById(Integer id);
    PageInfo<StudentAdminClass> selectPage(StudentAdminClass studentAdminClass, Integer pageNum, Integer pageSize);
    List<StudentAdminClass> selectAll(StudentAdminClass studentAdminClass);
    void addBatch(List<StudentAdminClass> list);
    void deleteByStudentIdAndClassId(String studentId, Integer adminClassId);
}
