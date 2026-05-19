package com.lgs.service.impl;

import com.lgs.entity.StudentCourse;
import com.lgs.entity.TeachingClass;
import com.lgs.mapper.StudentCourseMapper;
import com.lgs.mapper.TeachingClassMapper;
import com.lgs.service.StudentCourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Resource
    private StudentCourseMapper studentCourseMapper;

    @Resource
    private TeachingClassMapper teachingClassMapper;

    @Override
    public void add(StudentCourse studentCourse) {
        // 先检查是否已存在关联
        List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
        if (existing.isEmpty()) {
            studentCourseMapper.insert(studentCourse);
            
            // 如果指定了教学班，更新教学班人数
            if (studentCourse.getTeaching_class_id() != null) {
                TeachingClass teachingClass = teachingClassMapper.selectById(studentCourse.getTeaching_class_id());
                if (teachingClass != null) {
                    teachingClass.setSelected_count(teachingClass.getSelected_count() + 1);
                    teachingClassMapper.updateById(teachingClass);
                }
            }
        }
    }

    @Override
    public List<StudentCourse> selectAll(StudentCourse studentCourse) {
        return studentCourseMapper.selectAll(studentCourse);
    }

    @Override
    public void updateScore(StudentCourse studentCourse) {
        // 先检查是否已存在关联
        List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
        if (!existing.isEmpty()) {
            studentCourseMapper.updateScore(studentCourse);
        }
    }

    @Override
    public void deleteById(Integer id) {
        // 获取要删除的选课记录
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setId(id);
        List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
        if (!existing.isEmpty()) {
            StudentCourse sc = existing.get(0);
            // 如果有关联教学班，先记录教学班ID
            Integer teachingClassId = sc.getTeaching_class_id();
            
            studentCourseMapper.deleteById(id);
            
            // 更新教学班人数
            if (teachingClassId != null) {
                TeachingClass teachingClass = teachingClassMapper.selectById(teachingClassId);
                if (teachingClass != null && teachingClass.getSelected_count() > 0) {
                    teachingClass.setSelected_count(teachingClass.getSelected_count() - 1);
                    teachingClassMapper.updateById(teachingClass);
                }
            }
        } else {
            studentCourseMapper.deleteById(id);
        }
    }

    @Override
    public void deleteByStudentIdAndCourseId(StudentCourse studentCourse) {
        // 先查询要删除的记录
        List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
        if (!existing.isEmpty()) {
            StudentCourse sc = existing.get(0);
            // 如果有关联教学班，先记录教学班ID
            Integer teachingClassId = sc.getTeaching_class_id();
            
            studentCourseMapper.deleteByStudentIdAndCourseId(studentCourse);
            
            // 更新教学班人数
            if (teachingClassId != null) {
                TeachingClass teachingClass = teachingClassMapper.selectById(teachingClassId);
                if (teachingClass != null && teachingClass.getSelected_count() > 0) {
                    teachingClass.setSelected_count(teachingClass.getSelected_count() - 1);
                    teachingClassMapper.updateById(teachingClass);
                }
            }
        } else {
            studentCourseMapper.deleteByStudentIdAndCourseId(studentCourse);
        }
    }

    @Override
    public List<StudentCourse> selectByCourseId(Integer course_id) {
        return studentCourseMapper.selectByCourseId(course_id);
    }

    @Override
    public List<StudentCourse> selectByStudentId(String student_id) {
        return studentCourseMapper.selectByStudentId(student_id);
    }

    @Override
    public List<StudentCourse> selectByTeachingClassId(Integer teaching_class_id) {
        return studentCourseMapper.selectByTeachingClassId(teaching_class_id);
    }

    @Override
    public StudentCourse selectByStudentIdAndCourseId(String student_id, Integer course_id) {
        return studentCourseMapper.selectByStudentIdAndCourseId(student_id, course_id);
    }

    @Override
    public double calculateTotalScore(double usualScore, double midtermScore, double finalScore, Integer usualWeight, Integer midtermWeight, Integer finalWeight) {
        return (usualScore * usualWeight / 100) + (midtermScore * midtermWeight / 100) + (finalScore * finalWeight / 100);
    }

    @Override
    public List<StudentCourse> selectLowScoreCourses(double threshold) {
        return studentCourseMapper.selectLowScoreCourses(threshold);
    }

    @Override
    public void updateById(StudentCourse studentCourse) {
        studentCourseMapper.updateById(studentCourse);
    }
}