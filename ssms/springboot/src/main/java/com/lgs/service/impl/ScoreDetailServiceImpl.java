package com.lgs.service.impl;

import com.lgs.entity.ScoreDetail;
import com.lgs.mapper.ScoreDetailMapper;
import com.lgs.service.ScoreDetailService;
import com.lgs.service.ScoreRuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreDetailServiceImpl implements ScoreDetailService {

    @Resource
    private ScoreDetailMapper scoreDetailMapper;

    @Resource
    private ScoreRuleService scoreRuleService;

    @Override
    public void add(ScoreDetail scoreDetail) {
        // 计算总评成绩
        calculateTotalScore(scoreDetail);
        scoreDetailMapper.insert(scoreDetail);
    }

    @Override
    public void updateById(ScoreDetail scoreDetail) {
        // 计算总评成绩
        calculateTotalScore(scoreDetail);
        scoreDetailMapper.updateById(scoreDetail);
    }

    @Override
    public void deleteById(Integer id) {
        scoreDetailMapper.deleteById(id);
    }

    @Override
    public ScoreDetail selectById(Integer id) {
        return scoreDetailMapper.selectById(id);
    }

    @Override
    public List<ScoreDetail> selectAll(ScoreDetail scoreDetail) {
        return scoreDetailMapper.selectAll(scoreDetail);
    }

    @Override
    public ScoreDetail selectByStudentAndCourse(String student_id, Integer course_id) {
        return scoreDetailMapper.selectByStudentAndCourse(student_id, course_id);
    }

    @Override
    public ScoreDetail selectByStudentAndTeachingClass(String student_id, Integer teaching_class_id) {
        return scoreDetailMapper.selectByStudentAndTeachingClass(student_id, teaching_class_id);
    }

    @Override
    public List<ScoreDetail> selectByCourseId(Integer course_id) {
        return scoreDetailMapper.selectByCourseId(course_id);
    }

    @Override
    public List<ScoreDetail> selectByTeachingClassId(Integer teaching_class_id) {
        return scoreDetailMapper.selectByTeachingClassId(teaching_class_id);
    }

    @Override
    public List<ScoreDetail> selectByStudentId(String student_id) {
        return scoreDetailMapper.selectByStudentId(student_id);
    }

    @Override
    public List<ScoreDetail> getWarningStudents(Integer teaching_class_id) {
        List<ScoreDetail> scoreDetails = scoreDetailMapper.selectByTeachingClassId(teaching_class_id);
        List<ScoreDetail> warningStudents = new ArrayList<>();
        
        // 获取预警阈值
        Integer warning_threshold = 60; // 默认阈值
        if (!scoreDetails.isEmpty()) {
            // 从第一个成绩详情中获取课程ID
            Integer course_id = scoreDetails.get(0).getCourse_id();
            var scoreRule = scoreRuleService.selectByCourseId(course_id);
            if (scoreRule != null) {
                warning_threshold = scoreRule.getWarning_threshold();
            }
        }
        
        // 筛选预警学生
        for (ScoreDetail scoreDetail : scoreDetails) {
            if (scoreDetail.getTotal_score() < warning_threshold) {
                warningStudents.add(scoreDetail);
            }
        }
        
        return warningStudents;
    }

    @Override
    public double calculateTotalScore(double usual_score, double midterm_score, double final_score, Integer usual_weight, Integer midterm_weight, Integer final_weight) {
        return (usual_score * usual_weight / 100) + (midterm_score * midterm_weight / 100) + (final_score * final_weight / 100);
    }

    // 计算总评成绩
    private void calculateTotalScore(ScoreDetail scoreDetail) {
        Integer course_id = scoreDetail.getCourse_id();
        var scoreRule = scoreRuleService.selectByCourseId(course_id);
        
        // 默认权重
        int usual_weight = 30;
        int midterm_weight = 20;
        int final_weight = 50;
        
        if (scoreRule != null) {
            usual_weight = scoreRule.getUsual_weight();
            midterm_weight = scoreRule.getMidterm_weight();
            final_weight = scoreRule.getFinal_weight();
        }
        
        double total_score = calculateTotalScore(
            scoreDetail.getUsual_score() != null ? scoreDetail.getUsual_score() : 0,
            scoreDetail.getMidterm_score() != null ? scoreDetail.getMidterm_score() : 0,
            scoreDetail.getFinal_score() != null ? scoreDetail.getFinal_score() : 0,
            usual_weight, midterm_weight, final_weight
        );
        
        scoreDetail.setTotal_score(total_score);
    }
}