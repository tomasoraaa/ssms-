package com.lgs.service.impl;

import com.lgs.entity.StudentCourse;
import com.lgs.mapper.StudentCourseMapper;
import com.lgs.service.StudentCourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Resource
    private StudentCourseMapper studentCourseMapper;

    @Override
    public void add(StudentCourse studentCourse) {
        // 先检查是否已存在关联
        List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
        if (existing.isEmpty()) {
            studentCourseMapper.insert(studentCourse);
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
        studentCourseMapper.deleteById(id);
    }

    @Override
    public void deleteByStudentIdAndCourseId(StudentCourse studentCourse) {
        studentCourseMapper.deleteByStudentIdAndCourseId(studentCourse);
    }

    @Override
    public List<StudentCourse> selectByCourseId(String courseId) {
        return studentCourseMapper.selectByCourseId(courseId);
    }

    @Override
    public List<StudentCourse> selectByStudentId(String studentId) {
        return studentCourseMapper.selectByStudentId(studentId);
    }

    @Override
    public List<StudentCourse> selectByTeachingClassId(Integer teachingClassId) {
        return studentCourseMapper.selectByTeachingClassId(teachingClassId);
    }

    @Override
    public double calculateTotalScore(double usualScore, double midtermScore, double finalScore, Integer usualWeight, Integer midtermWeight, Integer finalWeight) {
        return (usualScore * usualWeight / 100) + (midtermScore * midtermWeight / 100) + (finalScore * finalWeight / 100);
    }
}