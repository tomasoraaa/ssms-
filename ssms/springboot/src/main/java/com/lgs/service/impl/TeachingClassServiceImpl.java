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
        if (teachingClass.getSelected_count() == null) {
            teachingClass.setSelected_count(0);
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
    public List<TeachingClass> selectByAcademicYearId(Integer academic_year_id) {
        return teachingClassMapper.selectByAcademicYearId(academic_year_id);
    }

    @Override
    public List<TeachingClass> selectAvailable(Integer academic_year_id) {
        return teachingClassMapper.selectAvailable(academic_year_id);
    }

    @Override
    public List<TeachingClass> selectByCourseId(Integer course_id) {
        TeachingClass teachingClass = new TeachingClass();
        teachingClass.setCourse_id(course_id);
        return teachingClassMapper.selectAll(teachingClass);
    }
}
