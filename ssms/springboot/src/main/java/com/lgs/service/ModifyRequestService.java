package com.lgs.service;

import com.github.pagehelper.PageInfo;
import com.lgs.entity.ModifyRequest;

public interface ModifyRequestService {
    void add(ModifyRequest modifyRequest);
    PageInfo<ModifyRequest> selectPage(ModifyRequest modifyRequest, Integer pageNum, Integer pageSize);
    void updateById(ModifyRequest modifyRequest);
    void deleteById(Integer id);
    ModifyRequest selectById(Integer id);
    void approve(Integer id);
    void reject(Integer id);
}