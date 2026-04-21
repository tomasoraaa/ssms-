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
        courseEvaluation.setEvaluation_time(new Date());
        CourseEvaluation existing = courseEvaluationMapper.selectByStudentAndCourse(
                courseEvaluation.getStudent_id(), courseEvaluation.getCourse_id());
        if (existing != null) {
            return courseEvaluationMapper.update(courseEvaluation) > 0;
        } else {
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
    public CourseEvaluation getByStudentAndCourse(String student_id, Integer course_id) {
        return courseEvaluationMapper.selectByStudentAndCourse(student_id, course_id);
    }

    @Override
    public List<CourseEvaluation> getByCourseId(Integer course_id) {
        return courseEvaluationMapper.selectByCourseId(course_id);
    }

    @Override
    public List<CourseEvaluation> getByTeacherId(String teacher_id) {
        return courseEvaluationMapper.selectByTeacherId(teacher_id);
    }

    @Override
    public int count() {
        return courseEvaluationMapper.count();
    }
}