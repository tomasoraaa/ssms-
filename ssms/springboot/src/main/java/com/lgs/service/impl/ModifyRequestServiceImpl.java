package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.ModifyRequest;
import com.lgs.entity.Student;
import com.lgs.entity.Teacher;
import com.lgs.mapper.ModifyRequestMapper;
import com.lgs.mapper.StudentMapper;
import com.lgs.mapper.TeacherMapper;
import com.lgs.service.ModifyRequestService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModifyRequestServiceImpl implements ModifyRequestService {

    @Resource
    private ModifyRequestMapper modifyRequestMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public void add(ModifyRequest modifyRequest) {
        modifyRequest.setStatus(0); // 0-待审核
        modifyRequestMapper.insert(modifyRequest);
    }

    @Override
    public PageInfo<ModifyRequest> selectPage(ModifyRequest modifyRequest, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ModifyRequest> list = modifyRequestMapper.selectAll(modifyRequest);
        return PageInfo.of(list);
    }

    @Override
    public void updateById(ModifyRequest modifyRequest) {
        modifyRequestMapper.updateById(modifyRequest);
    }

    @Override
    public void deleteById(Integer id) {
        modifyRequestMapper.deleteById(id);
    }

    @Override
    public ModifyRequest selectById(Integer id) {
        return modifyRequestMapper.selectById(id);
    }

    @Override
    public void approve(Integer id) {
        // 获取修改申请
        ModifyRequest modifyRequest = modifyRequestMapper.selectById(id);
        if (modifyRequest != null) {
            // 更新申请状态为已通过
            modifyRequest.setStatus(1);
            modifyRequestMapper.updateById(modifyRequest);

            // 根据用户类型更新相应的用户信息
            if ("STUDENT".equals(modifyRequest.getUser_type())) {
                Student student = new Student();
                student.setUsername(modifyRequest.getUser_id());
                List<Student> students = studentMapper.selectAll(student);
                if (!students.isEmpty()) {
                    student = students.get(0);
                    updateStudentField(student, modifyRequest.getField_name(), modifyRequest.getNew_value());
                    studentMapper.updateById(student);
                }
            } else if ("TEACHER".equals(modifyRequest.getUser_type())) {
                Teacher teacher = new Teacher();
                teacher.setUsername(modifyRequest.getUser_id());
                List<Teacher> teachers = teacherMapper.selectAll(teacher);
                if (!teachers.isEmpty()) {
                    teacher = teachers.get(0);
                    updateTeacherField(teacher, modifyRequest.getField_name(), modifyRequest.getNew_value());
                    teacherMapper.updateById(teacher);
                }
            }
        }
    }

    @Override
    public void reject(Integer id) {
        // 更新申请状态为已拒绝
        ModifyRequest modifyRequest = new ModifyRequest();
        modifyRequest.setId(id);
        modifyRequest.setStatus(2);
        modifyRequestMapper.updateById(modifyRequest);
    }

    // 更新学生字段
    private void updateStudentField(Student student, String fieldName, String newValue) {
        switch (fieldName) {
            case "phone":
                student.setPhone(newValue);
                break;
            case "email":
                student.setEmail(newValue);
                break;
            case "avatar":
                student.setAvatar(newValue);
                break;
        }
    }

    // 更新教师字段
    private void updateTeacherField(Teacher teacher, String fieldName, String newValue) {
        switch (fieldName) {
            case "phone":
                teacher.setPhone(newValue);
                break;
            case "email":
                teacher.setEmail(newValue);
                break;
            case "avatar":
                teacher.setAvatar(newValue);
                break;
        }
    }
}