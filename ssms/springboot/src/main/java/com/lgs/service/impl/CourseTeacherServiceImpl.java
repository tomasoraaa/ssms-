package com.lgs.service.impl;

import com.lgs.entity.CourseTeacher;
import com.lgs.mapper.CourseTeacherMapper;
import com.lgs.service.CourseTeacherService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Resource
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public void add(CourseTeacher courseTeacher) {
        courseTeacherMapper.insert(courseTeacher);
    }

    @Override
    public List<CourseTeacher> selectByCourseId(Integer courseId) {
        return courseTeacherMapper.selectByCourseId(courseId);
    }

    @Override
    public List<CourseTeacher> selectByTeacherId(String teacherId) {
        return courseTeacherMapper.selectByTeacherId(teacherId);
    }

    @Override
    public List<CourseTeacher> selectByTeachingClassId(Integer teachingClassId) {
        return courseTeacherMapper.selectByTeachingClassId(teachingClassId);
    }

    @Override
    public void deleteByCourseIdAndTeacherId(Integer courseId, String teacherId) {
        courseTeacherMapper.deleteByCourseIdAndTeacherId(courseId, teacherId);
    }

    @Override
    public void deleteByTeachingClassId(Integer teachingClassId) {
        courseTeacherMapper.deleteByTeachingClassId(teachingClassId);
    }

    @Override
    public void updateById(CourseTeacher courseTeacher) {
        courseTeacherMapper.updateById(courseTeacher);
    }
}
