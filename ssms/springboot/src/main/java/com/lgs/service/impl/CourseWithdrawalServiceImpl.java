package com.lgs.service.impl;

import com.lgs.entity.CourseWithdrawal;
import com.lgs.entity.StudentCourse;
import com.lgs.entity.TeachingClass;
import com.lgs.mapper.CourseWithdrawalMapper;
import com.lgs.mapper.StudentCourseMapper;
import com.lgs.mapper.TeachingClassMapper;
import com.lgs.service.CourseWithdrawalService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseWithdrawalServiceImpl implements CourseWithdrawalService {

    @Resource
    private CourseWithdrawalMapper courseWithdrawalMapper;

    @Resource
    private StudentCourseMapper studentCourseMapper;

    @Resource
    private TeachingClassMapper teachingClassMapper;

    @Override
    public void add(CourseWithdrawal courseWithdrawal) {
        // 设置退课时间为当前时间
        courseWithdrawal.setWithdrawal_time(new java.util.Date());
        courseWithdrawalMapper.insert(courseWithdrawal);
    }

    @Override
    public List<CourseWithdrawal> selectAll(CourseWithdrawal courseWithdrawal) {
        return courseWithdrawalMapper.selectAll(courseWithdrawal);
    }

    @Override
    public List<CourseWithdrawal> selectByStudentId(String studentId) {
        return courseWithdrawalMapper.selectByStudentId(studentId);
    }

    @Override
    public void updateById(CourseWithdrawal courseWithdrawal) {
        courseWithdrawalMapper.updateById(courseWithdrawal);
    }

    @Override
    public void deleteById(Integer id) {
        courseWithdrawalMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void approve(Integer id) {
        try {
            // 更新退课申请状态为已批准
            CourseWithdrawal courseWithdrawal = new CourseWithdrawal();
            courseWithdrawal.setId(id);
            courseWithdrawal.setStatus(1);
            courseWithdrawalMapper.updateById(courseWithdrawal);

            // 查询退课申请详情
            CourseWithdrawal withdrawal = new CourseWithdrawal();
            withdrawal.setId(id);
            List<CourseWithdrawal> withdrawals = courseWithdrawalMapper.selectAll(withdrawal);
            if (!withdrawals.isEmpty()) {
                CourseWithdrawal selected = withdrawals.get(0);
                // 更新学生课程状态为已退课
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudent_id(selected.getStudent_id());
                studentCourse.setCourse_id(selected.getCourse_id());
                List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
                if (!existing.isEmpty()) {
                    StudentCourse sc = existing.get(0);
                    // 保存教学班级ID，用于后续更新
                    Integer teachingClassId = sc.getTeaching_class_id();
                    
                    // 直接删除记录模拟退课
                    studentCourseMapper.deleteByStudentIdAndCourseId(studentCourse);
                    
                    // 减少教学班的选中人数
                    if (teachingClassId != null) {
                        TeachingClass teachingClass = teachingClassMapper.selectById(teachingClassId);
                        if (teachingClass != null && teachingClass.getSelected_count() > 0) {
                            teachingClass.setSelected_count(teachingClass.getSelected_count() - 1);
                            teachingClassMapper.updateById(teachingClass);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 事务会自动回滚
            throw new RuntimeException("退课操作失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void reject(Integer id) {
        // 更新退课申请状态为已拒绝
        CourseWithdrawal courseWithdrawal = new CourseWithdrawal();
        courseWithdrawal.setId(id);
        courseWithdrawal.setStatus(2);
        courseWithdrawalMapper.updateById(courseWithdrawal);
    }
}