package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.StudentAdminClass;
import com.lgs.mapper.StudentAdminClassMapper;
import com.lgs.service.StudentAdminClassService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentAdminClassServiceImpl implements StudentAdminClassService {

    @Resource
    private StudentAdminClassMapper studentAdminClassMapper;

    @Override
    public void add(StudentAdminClass studentAdminClass) {
        studentAdminClassMapper.insert(studentAdminClass);
    }

    @Override
    public void updateById(StudentAdminClass studentAdminClass) {
        studentAdminClassMapper.updateById(studentAdminClass);
    }

    @Override
    public void deleteById(Integer id) {
        studentAdminClassMapper.deleteById(id);
    }

    @Override
    public StudentAdminClass selectById(Integer id) {
        return studentAdminClassMapper.selectById(id);
    }

    @Override
    public PageInfo<StudentAdminClass> selectPage(StudentAdminClass studentAdminClass, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentAdminClass> list = studentAdminClassMapper.selectAll(studentAdminClass);
        return PageInfo.of(list);
    }

    @Override
    public List<StudentAdminClass> selectAll(StudentAdminClass studentAdminClass) {
        return studentAdminClassMapper.selectAll(studentAdminClass);
    }

    @Override
    public void addBatch(List<StudentAdminClass> list) {
        for (StudentAdminClass sac : list) {
            studentAdminClassMapper.insert(sac);
        }
    }

    @Override
    public void deleteByStudentIdAndClassId(String studentId, Integer adminClassId) {
        studentAdminClassMapper.deleteByStudentIdAndClassId(studentId, adminClassId);
    }
}
