package com.lgs.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.lgs.entity.Account;
import com.lgs.entity.Admin;
import com.lgs.exception.CustomException;
import com.lgs.mapper.AdminMapper;
import com.lgs.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 管理员业务处理实现
 **/
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 新增
     */
    @Override
    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (ObjectUtil.isNotNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword("admin");
        }
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    /**
     * 删除
     */
    @Override
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    /**
     * 修改
     */
    @Override
    public void updateById(Admin admin) {
        adminMapper.updateById(admin);
    }

    /**
     * 根据ID查询
     */
    @Override
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    @Override
    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    /**
     * 分页查询
     */
    @Override
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    @Override
    public Account login(Account account) {
        Account dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        return dbAdmin;
    }

    /**
     * 修改密码
     */
    @Override
    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException("原密码错误");
        }
        dbAdmin.setPassword(account.getNewPassword());
        adminMapper.updateById(dbAdmin);
    }

    /**
     * 批量导入
     */
    @Override
    public void importExcel(MultipartFile file) {
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            List<List<Object>> rows = reader.read();
            for (int i = 1; i < rows.size(); i++) {
                List<Object> row = rows.get(i);
                if (row.size() >= 2) {
                    Admin admin = new Admin();
                    if (row.get(0) != null) {
                        admin.setUsername(row.get(0).toString());
                    }
                    if (row.get(1) != null) {
                        admin.setName(row.get(1).toString());
                    }
                    if (row.size() > 2 && row.get(2) != null) {
                        admin.setPassword(row.get(2).toString());
                    } else {
                        admin.setPassword("admin");
                    }
                    if (row.size() > 3 && row.get(3) != null) {
                        admin.setAvatar(row.get(3).toString());
                    }
                    admin.setRole("ADMIN");
                    Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
                    if (ObjectUtil.isNull(dbAdmin)) {
                        adminMapper.insert(admin);
                    } else {
                        admin.setId(dbAdmin.getId());
                        adminMapper.updateById(admin);
                    }
                }
            }
        } catch (Exception e) {
            throw new CustomException("批量导入失败：" + e.getMessage());
        }
    }

}