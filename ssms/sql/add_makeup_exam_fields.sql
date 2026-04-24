-- 为缓考/补考功能增加字段
-- original_score: 原始不及格成绩
-- is_final: 是否为最终成绩（0-需加权计算，1-直接作为最终成绩）

ALTER TABLE makeup_exam ADD COLUMN original_score DECIMAL(5,2) COMMENT '原始不及格成绩';

ALTER TABLE makeup_exam ADD COLUMN is_final TINYINT(1) DEFAULT 0 COMMENT '是否为最终成绩（0-需加权计算，1-直接作为最终成绩）';