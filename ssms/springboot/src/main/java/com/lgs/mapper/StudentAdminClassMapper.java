package com.lgs.mapper;

import com.lgs.entity.StudentAdminClass;
import java.util.List;

public interface StudentAdminClassMapper {
    void insert(StudentAdminClass studentAdminClass);
    void updateById(StudentAdminClass studentAdminClass);
    void deleteById(Integer id);
    StudentAdminClass selectById(Integer id);
    List<StudentAdminClass> selectAll(StudentAdminClass studentAdminClass);
    void deleteByStudentIdAndClassId(String studentId, Integer adminClassId);
}
