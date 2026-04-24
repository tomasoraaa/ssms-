package com.lgs.service.impl;

import com.lgs.entity.ScoreRule;
import com.lgs.mapper.ScoreRuleMapper;
import com.lgs.service.ScoreRuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreRuleServiceImpl implements ScoreRuleService {

    @Resource
    private ScoreRuleMapper scoreRuleMapper;

    @Override
    public void add(ScoreRule scoreRule) {
        // 验证权重之和为100
        if (scoreRule.getUsual_weight() + scoreRule.getMidterm_weight() + scoreRule.getFinal_weight() != 100) {
            throw new RuntimeException("权重之和必须为100%");
        }
        scoreRuleMapper.insert(scoreRule);
    }

    @Override
    public void updateById(ScoreRule scoreRule) {
        // 验证权重之和为100
        if (scoreRule.getUsual_weight() + scoreRule.getMidterm_weight() + scoreRule.getFinal_weight() != 100) {
            throw new RuntimeException("权重之和必须为100%");
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
        scoreRuleMapper.deleteById(id);
    }

}