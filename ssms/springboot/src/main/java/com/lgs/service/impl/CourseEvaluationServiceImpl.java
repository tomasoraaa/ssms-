package com.lgs.service.impl;

import com.lgs.entity.CourseEvaluation;
import com.lgs.mapper.CourseEvaluationMapper;
import com.lgs.service.CourseEvaluationService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CourseEvaluationServiceImpl implements CourseEvaluationService {

    @Resource
    private CourseEvaluationMapper courseEvaluationMapper;

    @Override
    public boolean saveOrUpdate(CourseEvaluation courseEvaluation) {
        courseEvaluation.setEvaluationTime(new Date());
        CourseEvaluation existing = courseEvaluationMapper.selectByStudentAndCourse(
                courseEvaluation.getStudentId(), courseEvaluation.getCourseId());
        if (existing != null) {
            // 更新现有评价
            return courseEvaluationMapper.update(courseEvaluation) > 0;
        } else {
            // 添加新评价
            return courseEvaluationMapper.insert(courseEvaluation) > 0;
        }
    }
    
    @Override
    public List<CourseEvaluation> getAll() {
        return courseEvaluationMapper.selectAll();
    }
    
    @Override
    public boolean deleteById(Integer id) {
        return courseEvaluationMapper.deleteById(id) > 0;
    }

    @Override
    public CourseEvaluation getByStudentAndCourse(String studentId, Integer courseId) {
        return courseEvaluationMapper.selectByStudentAndCourse(studentId, courseId);
    }

    @Override
    public List<CourseEvaluation> getByCourseId(Integer courseId) {
        return courseEvaluationMapper.selectByCourseId(courseId);
    }

    @Override
    public List<CourseEvaluation> getByTeacherId(String teacherId) {
        return courseEvaluationMapper.selectByTeacherId(teacherId);
    }
    
    @Override
    public int count() {
        return courseEvaluationMapper.count();
    }
}
