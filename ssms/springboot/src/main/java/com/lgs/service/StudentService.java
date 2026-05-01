package com.lgs.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.common.PasswordUtil;
import com.lgs.entity.Account;
import com.lgs.entity.Student;
import com.lgs.exception.CustomException;
import java.util.ArrayList;
import com.lgs.mapper.StudentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;

    public void add(Student student) {
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());
        if (ObjectUtil.isNotNull(dbStudent)) {
            throw new CustomException("该学号对应学生已存在");
        }
        if (ObjectUtil.isEmpty(student.getPassword())) {
            student.setPassword(PasswordUtil.encode("123456"));
        } else {
            student.setPassword(PasswordUtil.encode(student.getPassword()));
        }
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }
        student.setRole("STUDENT");
        if (student.getStatus() == null) {
            student.setStatus(1); // 管理员直接添加的学生默认审核通过
        }
        studentMapper.insert(student);
    }
    public PageInfo<Student> selectPage(Student student, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> list = studentMapper.selectAll(student);
        return PageInfo.of(list);
    }

    public void updateById(Student student) {
        // 检查用户名是否已存在（排除当前记录）
        Student existingStudent = studentMapper.selectByUsername(student.getUsername());
        if (existingStudent != null && !existingStudent.getId().equals(student.getId())) {
            throw new CustomException("用户名已存在，请重新输入");
        }
        studentMapper.updateById(student);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * 查询所有
     */
    public List<Student> selectAll(Student student) {
        return studentMapper.selectAll(student);
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Student student = new Student();
        if (account instanceof com.lgs.entity.Account) {
            com.lgs.entity.Account ac = (com.lgs.entity.Account) account;
            student.setUsername(ac.getUsername());
            student.setPassword(ac.getPassword());
            student.setName(ac.getName());
            student.setAge(ac.getAge());
            student.setGender(ac.getGender());
            student.setPhone(ac.getPhone());
            student.setProfession(ac.getProfession());
            student.setRole("STUDENT");
            student.setStatus(0); // 0-待审核
        }
        add(student);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Student student = studentMapper.selectByUsername(account.getUsername());
        if (student == null) {
            throw new CustomException("用户不存在");
        }
        if (!PasswordUtil.matches(account.getPassword(), student.getPassword())) {
            throw new CustomException("密码错误");
        }
        if (student.getStatus() != 1) {
            throw new CustomException("账号未审核通过，无法登录");
        }
        return student;
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
                    Student student = new Student();
                    // 第一列：学号
                    if (row.get(0) != null) {
                        student.setUsername(row.get(0).toString());
                    }
                    // 第二列：姓名
                    if (row.get(1) != null) {
                        student.setName(row.get(1).toString());
                    }
                    // 第三列：密码（可选）
                    if (row.size() > 2 && row.get(2) != null) {
                        student.setPassword(PasswordUtil.encode(row.get(2).toString()));
                    } else {
                        student.setPassword(PasswordUtil.encode("123456")); // 默认密码
                    }
                    // 第四列：年龄（可选）
                    if (row.size() > 3 && row.get(3) != null) {
                        student.setAge(row.get(3).toString());
                    }
                    // 第五列：性别（可选）
                    if (row.size() > 4 && row.get(4) != null) {
                        student.setGender(row.get(4).toString());
                    }
                    // 第六列：手机号（可选）
                    if (row.size() > 5 && row.get(5) != null) {
                        student.setPhone(row.get(5).toString());
                    }
                    // 第七列：专业（可选）
                    if (row.size() > 6 && row.get(6) != null) {
                        student.setProfession(row.get(6).toString());
                    }
                    // 第八列：头像（可选）
                    if (row.size() > 7 && row.get(7) != null) {
                        student.setAvatar(row.get(7).toString());
                    }
                    // 设置角色
                    student.setRole("STUDENT");
                    // 设置状态为已通过（管理员批量导入）
                    student.setStatus(1);
                    // 检查用户是否已存在
                    Student dbStudent = studentMapper.selectByUsername(student.getUsername());
                    if (ObjectUtil.isNull(dbStudent)) {
                        // 新增用户
                        studentMapper.insert(student);
                    } else {
                        // 更新用户信息
                        student.setId(dbStudent.getId());
                        studentMapper.updateById(student);
                    }
                }
            }
        } catch (Exception e) {
            throw new CustomException("批量导入失败：" + e.getMessage());
        }
    }
    
    /**
     * 统计学生数量
     */
    public int count() {
        return studentMapper.count();
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException("用户不存在");
        }
        if (!PasswordUtil.matches(account.getPassword(), dbStudent.getPassword())) {
            throw new CustomException("原密码错误");
        }
        dbStudent.setPassword(PasswordUtil.encode(account.getNewPassword()));
        studentMapper.updateById(dbStudent);
    }

    /**
     * 根据学生用户名列表查询
     */
    public List<Student> selectByUsernames(List<String> usernames) {
        // 验证输入参数
        if (usernames == null || usernames.isEmpty()) {
            return new ArrayList<>();
        }
        return studentMapper.selectByUsernames(usernames);
    }

    /**
     * 重置单个学生密码（重置为默认密码123456）
     */
    public void resetPassword(String username) {
        Student student = studentMapper.selectByUsername(username);
        if (student == null) {
            throw new CustomException("学生不存在");
        }
        student.setPassword(PasswordUtil.encode("123456"));
        studentMapper.updateById(student);
    }

    /**
     * 批量重置学生密码
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
                // 跳过失败的记录，继续处理其他学生
                continue;
            }
        }
        return count;
    }

    /**
     * 重置所有学生密码
     */
    public int resetAllPassword() {
        List<Student> students = studentMapper.selectAll(new Student());
        if (students == null || students.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Student student : students) {
            try {
                student.setPassword(PasswordUtil.encode("123456"));
                studentMapper.updateById(student);
                count++;
            } catch (Exception e) {
                continue;
            }
        }
        return count;
    }

    /**
     * 根据用户名查询学生（不验证密码）
     */
    public Account loginByUsername(String username) {
        return studentMapper.selectByUsername(username);
    }
}
