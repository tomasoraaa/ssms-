package com.lgs.service;

import com.github.pagehelper.PageInfo;
import com.lgs.entity.AdminClass;
import java.util.List;

public interface AdminClassService {
    void add(AdminClass adminClass);
    void updateById(AdminClass adminClass);
    void deleteById(Integer id);
    AdminClass selectById(Integer id);
    PageInfo<AdminClass> selectPage(AdminClass adminClass, Integer pageNum, Integer pageSize);
    List<AdminClass> selectAll(AdminClass adminClass);
}
