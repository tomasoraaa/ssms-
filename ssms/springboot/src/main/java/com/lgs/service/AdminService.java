package com.lgs.service;

import com.lgs.entity.Account;
import com.lgs.entity.Admin;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 管理员服务接口
 **/
public interface AdminService {

    /**
     * 新增
     */
    void add(Admin admin);

    /**
     * 删除
     */
    void deleteById(Integer id);

    /**
     * 修改
     */
    void updateById(Admin admin);

    /**
     * 根据ID查询
     */
    Admin selectById(Integer id);

    /**
     * 查询所有
     */
    List<Admin> selectAll(Admin admin);

    /**
     * 分页查询
     */
    PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize);

    /**
     * 登录
     */
    Account login(Account account);

    /**
     * 修改密码
     */
    void updatePassword(Account account);

    /**
     * 批量导入
     */
    void importExcel(MultipartFile file);

}