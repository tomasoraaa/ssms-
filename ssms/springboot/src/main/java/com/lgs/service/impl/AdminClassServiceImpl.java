package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.AdminClass;
import com.lgs.mapper.AdminClassMapper;
import com.lgs.service.AdminClassService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminClassServiceImpl implements AdminClassService {

    @Resource
    private AdminClassMapper adminClassMapper;

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
