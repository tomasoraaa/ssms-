package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.AcademicYear;
import com.lgs.mapper.AcademicYearMapper;
import com.lgs.service.AcademicYearService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AcademicYearServiceImpl implements AcademicYearService {

    @Resource
    private AcademicYearMapper academicYearMapper;

    @Override
    public void add(AcademicYear academicYear) {
        academicYearMapper.insert(academicYear);
    }

    @Override
    public void updateById(AcademicYear academicYear) {
        academicYearMapper.updateById(academicYear);
    }

    @Override
    public void deleteById(Integer id) {
        academicYearMapper.deleteById(id);
    }

    @Override
    public AcademicYear selectById(Integer id) {
        return academicYearMapper.selectById(id);
    }

    @Override
    public PageInfo<AcademicYear> selectPage(AcademicYear academicYear, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AcademicYear> list = academicYearMapper.selectAll(academicYear);
        return PageInfo.of(list);
    }

    @Override
    public List<AcademicYear> selectAll(AcademicYear academicYear) {
        return academicYearMapper.selectAll(academicYear);
    }

    @Override
    public AcademicYear selectCurrent() {
        return academicYearMapper.selectCurrent();
    }
}
