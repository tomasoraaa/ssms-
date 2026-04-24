package com.lgs.entity;

import lombok.Data;

@Data
public class ScoreRule {
    private Integer id;
    private Integer course_id;
    private Integer usual_weight;
    private Integer midterm_weight;
    private Integer final_weight;
    private Integer warning_threshold;
    private String created_at;
    private String updated_at;
}