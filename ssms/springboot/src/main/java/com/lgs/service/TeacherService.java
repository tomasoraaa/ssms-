package com.lgs.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.Account;
import com.lgs.entity.Teacher;
import com.lgs.exception.CustomException;
import com.lgs.mapper.TeacherMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    public void add(Teacher teacher) {
        Teacher dbTeacher = teacherMapper.selectByUsername(teacher.getUsername());
        if (ObjectUtil.isNotNull(dbTeacher)) {
            throw new CustomException("用户已存在");
        }
        if (ObjectUtil.isEmpty(teacher.getPassword())) {
            teacher.setPassword("123456");
        }
        if (ObjectUtil.isEmpty(teacher.getName())) {
            teacher.setName(teacher.getUsername());
        }
        teacher.setRole("TEACHER");
        if (teacher.getStatus() == null) {
            teacher.setStatus(1); // 管理员直接添加的教师默认审核通过
        }
        teacherMapper.insert(teacher);
    }
    public PageInfo<Teacher> selectPage(Teacher teacher, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> list = teacherMapper.selectAll(teacher);
        return PageInfo.of(list);
    }

    public void updateById(Teacher teacher) {
        // 检查用户名是否已存在（排除当前记录）
        Teacher existingTeacher = teacherMapper.selectByUsername(teacher.getUsername());
        if (existingTeacher != null && !existingTeacher.getId().equals(teacher.getId())) {
            throw new CustomException("用户名已存在，请重新输入");
        }
        teacherMapper.updateById(teacher);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        teacherMapper.deleteById(id);
    }

    /**
     * 注册
     */
    public void register(Object account) {
        Teacher teacher = new Teacher();
        if (account instanceof com.lgs.entity.Account) {
            com.lgs.entity.Account ac = (com.lgs.entity.Account) account;
            teacher.setUsername(ac.getUsername());
            teacher.setPassword(ac.getPassword());
            teacher.setName(ac.getName());
            teacher.setGender(ac.getGender());
            teacher.setPhone(ac.getPhone());
            teacher.setRole("TEACHER");
            teacher.setStatus(0); // 0-待审核
        }
        add(teacher);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Teacher teacher = teacherMapper.selectByUsername(account.getUsername());
        if (teacher == null) {
            throw new CustomException("用户不存在");
        }
        if (!teacher.getPassword().equals(account.getPassword())) {
            throw new CustomException("密码错误");
        }
        if (teacher.getStatus() != 1) {
            throw new CustomException("账号未审核通过，无法登录");
        }
        return teacher;
    }

    /**
     * 批量导入
     */
    public void importExcel(MultipartFile file) {
        try {
            // 使用Hutool的Excel工具类读取Excel文件
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            // 读取所有行数据
            List<List<Object>> rows = reader.read();
            // 跳过表头，从第二行开始处理数据
            for (int i = 1; i < rows.size(); i++) {
                List<Object> row = rows.get(i);
                if (row.size() >= 2) {
                    Teacher teacher = new Teacher();
                    // 第一列：教师工号
                    if (row.get(0) != null) {
                        teacher.setUsername(row.get(0).toString());
                    }
                    // 第二列：姓名
                    if (row.get(1) != null) {
                        teacher.setName(row.get(1).toString());
                    }
                    // 第三列：密码（可选）
                    if (row.size() > 2 && row.get(2) != null) {
                        teacher.setPassword(row.get(2).toString());
                    } else {
                        teacher.setPassword("123456"); // 默认密码
                    }
                    // 第四列：性别（可选）
                    if (row.size() > 3 && row.get(3) != null) {
                        teacher.setGender(row.get(3).toString());
                    }
                    // 第五列：手机号（可选）
                    if (row.size() > 4 && row.get(4) != null) {
                        teacher.setPhone(row.get(4).toString());
                    }
                    // 第六列：头像（可选）
                    if (row.size() > 5 && row.get(5) != null) {
                        teacher.setAvatar(row.get(5).toString());
                    }
                    // 设置角色
                    teacher.setRole("TEACHER");
                    // 设置状态为已通过（管理员批量导入）
                    teacher.setStatus(1);
                    // 检查用户是否已存在
                    Teacher dbTeacher = teacherMapper.selectByUsername(teacher.getUsername());
                    if (ObjectUtil.isNull(dbTeacher)) {
                        // 新增用户
                        teacherMapper.insert(teacher);
                    } else {
                        // 更新用户信息
                        teacher.setId(dbTeacher.getId());
                        teacherMapper.updateById(teacher);
                    }
                }
            }
        } catch (Exception e) {
            throw new CustomException("批量导入失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有教师信息
     */
    public List<Teacher> selectAll(Teacher teacher) {
        return teacherMapper.selectAll(teacher);
    }
    
    /**
     * 统计教师数量
     */
    public int count() {
        return teacherMapper.count();
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Teacher dbTeacher = teacherMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbTeacher)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbTeacher.getPassword())) {
            throw new CustomException("原密码错误");
        }
        dbTeacher.setPassword(account.getNewPassword());
        teacherMapper.updateById(dbTeacher);
    }

    /**
     * 重置单个教师密码（重置为默认密码123456）
     */
    public void resetPassword(String username) {
        Teacher teacher = teacherMapper.selectByUsername(username);
        if (teacher == null) {
            throw new CustomException("教师不存在");
        }
        teacher.setPassword("123456");
        teacherMapper.updateById(teacher);
    }

    /**
     * 批量重置教师密码
     */
    public int batchResetPassword(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (String username : usernames) {
            try {
                resetPassword(username);
                count++;
            } catch (Exception e) {
                // 跳过失败的记录，继续处理其他教师
                continue;
            }
        }
        return count;
    }

    /**
     * 重置所有教师密码
     */
    public int resetAllPassword() {
        List<Teacher> teachers = teacherMapper.selectAll(new Teacher());
        if (teachers == null || teachers.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Teacher teacher : teachers) {
            try {
                teacher.setPassword("123456");
                teacherMapper.updateById(teacher);
                count++;
            } catch (Exception e) {
                continue;
            }
        }
        return count;
    }

    /**
     * 根据用户名查询教师（不验证密码）
     */
    public Account loginByUsername(String username) {
        return teacherMapper.selectByUsername(username);
    }
}
