package com.lgs.mapper;

import com.lgs.entity.AdminClass;
import java.util.List;

public interface AdminClassMapper {
    void insert(AdminClass adminClass);
    void updateById(AdminClass adminClass);
    void deleteById(Integer id);
    AdminClass selectById(Integer id);
    List<AdminClass> selectAll(AdminClass adminClass);
}
