package com.lgs.service;

import com.lgs.entity.ScoreRule;

import java.util.List;

public interface ScoreRuleService {
    void add(ScoreRule scoreRule);
    void updateById(ScoreRule scoreRule);
    void deleteById(Integer id);
    ScoreRule selectById(Integer id);
    List<ScoreRule> selectAll(ScoreRule scoreRule);
    ScoreRule selectByCourseId(Integer course_id);
}