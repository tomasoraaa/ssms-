package com.lgs.mapper;

import com.lgs.entity.ScoreRule;

import java.util.List;

public interface ScoreRuleMapper {
    List<ScoreRule> selectAll(ScoreRule scoreRule);
    void insert(ScoreRule scoreRule);
    void updateById(ScoreRule scoreRule);
    void deleteById(Integer id);
    ScoreRule selectById(Integer id);
    ScoreRule selectByCourseId(Integer course_id);
}