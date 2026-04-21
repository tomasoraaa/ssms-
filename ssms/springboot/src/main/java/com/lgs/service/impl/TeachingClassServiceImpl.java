package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.TeachingClass;
import com.lgs.mapper.TeachingClassMapper;
import com.lgs.service.TeachingClassService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeachingClassServiceImpl implements TeachingClassService {

    @Resource
    private TeachingClassMapper teachingClassMapper;

    @Override
    public void add(TeachingClass teachingClass) {
        if (teachingClass.getSelectedCount() == null) {
            teachingClass.setSelectedCount(0);
        }
        if (teachingClass.getStatus() == null) {
            teachingClass.setStatus(1);
        }
        teachingClassMapper.insert(teachingClass);
    }

    @Override
    public void updateById(TeachingClass teachingClass) {
        teachingClassMapper.updateById(teachingClass);
    }

    @Override
    public void deleteById(Integer id) {
        teachingClassMapper.deleteById(id);
    }

    @Override
    public TeachingClass selectById(Integer id) {
        return teachingClassMapper.selectById(id);
    }

    @Override
    public PageInfo<TeachingClass> selectPage(TeachingClass teachingClass, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TeachingClass> list = teachingClassMapper.selectAll(teachingClass);
        return PageInfo.of(list);
    }

    @Override
    public List<TeachingClass> selectAll(TeachingClass teachingClass) {
        return teachingClassMapper.selectAll(teachingClass);
    }

    @Override
    public List<TeachingClass> selectByAcademicYearId(Integer academicYearId) {
        return teachingClassMapper.selectByAcademicYearId(academicYearId);
    }

    @Override
    public List<TeachingClass> selectAvailable(Integer academicYearId) {
        return teachingClassMapper.selectAvailable(academicYearId);
    }
}
