package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.AdminClass;
import com.lgs.entity.StudentAdminClass;
import com.lgs.exception.CustomException;
import com.lgs.mapper.AdminClassMapper;
import com.lgs.mapper.StudentAdminClassMapper;
import com.lgs.service.AdminClassService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminClassServiceImpl implements AdminClassService {

    @Resource
    private AdminClassMapper adminClassMapper;

    @Resource
    private StudentAdminClassMapper studentAdminClassMapper;

    @Override
    public void add(AdminClass adminClass) {
        adminClassMapper.insert(adminClass);
    }

    @Override
    public void updateById(AdminClass adminClass) {
        adminClassMapper.updateById(adminClass);
    }

    @Override
    public void deleteById(Integer id) {
        // 1. 检查是否有学生属于该班级
        StudentAdminClass studentAdminClass = new StudentAdminClass();
        studentAdminClass.setAdmin_class_id(id);
        List<StudentAdminClass> studentAdminClasses = studentAdminClassMapper.selectAll(studentAdminClass);
        if (!studentAdminClasses.isEmpty()) {
            throw new CustomException("该班级已有学生，无法删除");
        }
        
        // 2. 删除班级
        adminClassMapper.deleteById(id);
    }

    @Override
    public AdminClass selectById(Integer id) {
        return adminClassMapper.selectById(id);
    }

    @Override
    public PageInfo<AdminClass> selectPage(AdminClass adminClass, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AdminClass> list = adminClassMapper.selectAll(adminClass);
        return PageInfo.of(list);
    }

    @Override
    public List<AdminClass> selectAll(AdminClass adminClass) {
        return adminClassMapper.selectAll(adminClass);
    }
}
