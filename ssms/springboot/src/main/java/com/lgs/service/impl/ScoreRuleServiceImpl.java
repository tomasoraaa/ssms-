package com.lgs.service.impl;

import com.lgs.entity.ScoreDetail;
import com.lgs.entity.ScoreRule;
import com.lgs.exception.CustomException;
import com.lgs.mapper.ScoreDetailMapper;
import com.lgs.mapper.ScoreRuleMapper;
import com.lgs.service.ScoreDetailService;
import com.lgs.service.ScoreRuleService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreRuleServiceImpl implements ScoreRuleService {

    @Resource
    private ScoreRuleMapper scoreRuleMapper;

    @Resource
    @Lazy
    private ScoreDetailService scoreDetailService;

    @Override
    public void add(ScoreRule scoreRule) {
        // 验证权重之和为100
        if (scoreRule.getUsual_weight() + scoreRule.getMidterm_weight() + scoreRule.getFinal_weight() != 100) {
            throw new CustomException("权重之和必须为100%");
        }
        scoreRuleMapper.insert(scoreRule);
    }

    @Override
    public void updateById(ScoreRule scoreRule) {
        // 验证权重之和为100
        if (scoreRule.getUsual_weight() + scoreRule.getMidterm_weight() + scoreRule.getFinal_weight() != 100) {
            throw new CustomException("权重之和必须为100%");
        }
        scoreRuleMapper.updateById(scoreRule);
    }

    @Override
    public ScoreRule selectByCourseId(Integer course_id) {
        return scoreRuleMapper.selectByCourseId(course_id);
    }

    @Override
    public ScoreRule selectById(Integer id) {
        return scoreRuleMapper.selectById(id);
    }

    @Override
    public List<ScoreRule> selectAll(ScoreRule scoreRule) {
        return scoreRuleMapper.selectAll(scoreRule);
    }
    
    @Override
    public void deleteById(Integer id) {
        // 1. 查询成绩规则获取课程 ID
        ScoreRule scoreRule = scoreRuleMapper.selectById(id);
        if (scoreRule == null) {
            throw new CustomException("成绩规则不存在");
        }
        Integer courseId = scoreRule.getCourse_id();
        
        // 2. 检查是否有成绩记录
        List<ScoreDetail> scoreDetails = scoreDetailService.selectByCourseId(courseId);
        if (!scoreDetails.isEmpty()) {
            throw new CustomException("该课程已有成绩记录，无法删除成绩规则");
        }
        
        // 3. 删除成绩规则
        scoreRuleMapper.deleteById(id);
    }
}