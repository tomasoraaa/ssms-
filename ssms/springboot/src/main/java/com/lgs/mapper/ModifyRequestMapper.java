package com.lgs.mapper;

import com.lgs.entity.ModifyRequest;

import java.util.List;

public interface ModifyRequestMapper {
    List<ModifyRequest> selectAll(ModifyRequest modifyRequest);
    void insert(ModifyRequest modifyRequest);
    void updateById(ModifyRequest modifyRequest);
    void deleteById(Integer id);
    ModifyRequest selectById(Integer id);
}