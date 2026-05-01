package com.lgs.service.impl;

import com.lgs.entity.MakeupExam;
import com.lgs.entity.ScoreDetail;
import com.lgs.mapper.ScoreDetailMapper;
import com.lgs.service.MakeupExamService;
import com.lgs.service.ScoreDetailService;
import com.lgs.service.ScoreRuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreDetailServiceImpl implements ScoreDetailService {

    @Resource
    private ScoreDetailMapper scoreDetailMapper;

    @Resource
    private ScoreRuleService scoreRuleService;

    @Resource
    @org.springframework.context.annotation.Lazy
    private MakeupExamService makeupExamService;

    @Override
    public void add(ScoreDetail scoreDetail) {
        calculateTotalScore(scoreDetail);
        scoreDetailMapper.insert(scoreDetail);
    }

    @Override
    public void updateById(ScoreDetail scoreDetail) {
        calculateTotalScore(scoreDetail);
        scoreDetailMapper.updateById(scoreDetail);
        ScoreDetail updated = scoreDetailMapper.selectById(scoreDetail.getId());
        if (updated != null) {
            scoreDetail.setTotal_score(updated.getTotal_score());
            scoreDetail.setFinal_score(updated.getFinal_score());
        }
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
    public List<ScoreDetail> selectByStudentIdWithParams(String student_id, Integer course_id, Integer academic_year_id) {
        return scoreDetailMapper.selectByStudentIdWithParams(student_id, course_id, academic_year_id);
    }

    @Override
    public List<ScoreDetail> getWarningStudents(Integer teaching_class_id) {
        List<ScoreDetail> scoreDetails = scoreDetailMapper.selectByTeachingClassId(teaching_class_id);
        List<ScoreDetail> warningStudents = new ArrayList<>();

        Integer warning_threshold = 60;
        if (!scoreDetails.isEmpty()) {
            Integer course_id = scoreDetails.get(0).getCourse_id();
            var scoreRule = scoreRuleService.selectByCourseId(course_id);
            if (scoreRule != null) {
                warning_threshold = scoreRule.getWarning_threshold();
            }
        }

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

    private void calculateTotalScore(ScoreDetail scoreDetail) {
        Integer course_id = scoreDetail.getCourse_id();
        String student_id = scoreDetail.getStudent_id();

        // 默认不是补考
        scoreDetail.setIs_makeup(0);
        scoreDetail.setOriginal_score(null);
        scoreDetail.setMakeup_exam_type(null);

        if (student_id != null && course_id != null) {
            // 优化：按学生和课程查询补考记录，而不是查询所有记录
            MakeupExam makeupExamQuery = new MakeupExam();
            makeupExamQuery.setStudent_id(student_id);
            makeupExamQuery.setCourse_id(course_id);
            List<MakeupExam> makeupExams = makeupExamService.selectAll(makeupExamQuery);
            for (MakeupExam exam : makeupExams) {
                if (exam.getMakeup_score() != null) {

                    if ("补考".equals(exam.getExam_type())) {
                        // 保存原始成绩
                        Double existingTotalScore = scoreDetail.getTotal_score();
                        if (existingTotalScore == null || existingTotalScore == 0) {
                            // 如果没有现有的总评成绩，尝试从数据库查询
                            ScoreDetail existingDetail = scoreDetailMapper.selectByStudentAndCourse(student_id, course_id);
                            if (existingDetail != null && existingDetail.getTotal_score() != null) {
                                existingTotalScore = existingDetail.getTotal_score();
                            }
                        }
                        scoreDetail.setOriginal_score(existingTotalScore);
                        scoreDetail.setIs_makeup(1);
                        scoreDetail.setMakeup_exam_type("补考");
                        scoreDetail.setTotal_score(exam.getMakeup_score());
                        return;
                    } else if ("缓考".equals(exam.getExam_type())) {
                        scoreDetail.setIs_makeup(1);
                        scoreDetail.setMakeup_exam_type("缓考");
                        var scoreRule = scoreRuleService.selectByCourseId(course_id);
                        int usual_weight = 30, midterm_weight = 20, final_weight = 50;
                        if (scoreRule != null) {
                            usual_weight = scoreRule.getUsual_weight();
                            midterm_weight = scoreRule.getMidterm_weight();
                            final_weight = scoreRule.getFinal_weight();
                        }
                        double total_score = calculateTotalScore(
                            scoreDetail.getUsual_score() != null ? scoreDetail.getUsual_score() : 0,
                            scoreDetail.getMidterm_score() != null ? scoreDetail.getMidterm_score() : 0,
                            exam.getMakeup_score(),
                            usual_weight, midterm_weight, final_weight
                        );
                        scoreDetail.setTotal_score(total_score);
                        return;
                    }
                }
            }
        }

        var scoreRule = scoreRuleService.selectByCourseId(course_id);
        int usual_weight = 30, midterm_weight = 20, final_weight = 50;
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

    @Override
    public Map<String, Object> getClassScoreStatistics(Integer teaching_class_id) {
        Map<String, Object> statistics = new HashMap<>();
        List<ScoreDetail> scoreDetails = scoreDetailMapper.selectByTeachingClassId(teaching_class_id);
        
        if (scoreDetails == null || scoreDetails.isEmpty()) {
            statistics.put("averageScore", 0.0);
            statistics.put("maxScore", 0.0);
            statistics.put("minScore", 0.0);
            statistics.put("count", 0);
            return statistics;
        }
        
        double sum = 0.0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        int validCount = 0;
        
        for (ScoreDetail detail : scoreDetails) {
            Double score = detail.getTotal_score();
            if (score != null && score >= 0 && score <= 100) {
                sum += score;
                max = Math.max(max, score);
                min = Math.min(min, score);
                validCount++;
            }
        }
        
        statistics.put("averageScore", validCount > 0 ? Math.round(sum / validCount * 100.0) / 100.0 : 0.0);
        statistics.put("maxScore", validCount > 0 ? max : 0.0);
        statistics.put("minScore", validCount > 0 ? min : 0.0);
        statistics.put("count", validCount);
        
        return statistics;
    }

    @Override
    public Map<String, Object> getScoreDistribution(Integer teaching_class_id) {
        Map<String, Object> distribution = new HashMap<>();
        List<ScoreDetail> scoreDetails = scoreDetailMapper.selectByTeachingClassId(teaching_class_id);
        
        // 分数段定义
        int[] ranges = {0, 60, 70, 80, 90, 100};
        String[] rangeLabels = {"0-59", "60-69", "70-79", "80-89", "90-100"};
        int[] counts = new int[5];
        
        int totalCount = 0;
        for (ScoreDetail detail : scoreDetails) {
            Double score = detail.getTotal_score();
            if (score != null && score >= 0 && score <= 100) {
                totalCount++;
                for (int i = 0; i < ranges.length - 1; i++) {
                    if (i == ranges.length - 2) {
                        // 最后一个区间：90-100，包含100分
                        if (score >= ranges[i] && score <= ranges[i + 1]) {
                            counts[i]++;
                            break;
                        }
                    } else {
                        // 其他区间：左闭右开
                        if (score >= ranges[i] && score < ranges[i + 1]) {
                            counts[i]++;
                            break;
                        }
                    }
                }
            }
        }
        
        // 统计各分数段人数
        for (int i = 0; i < rangeLabels.length; i++) {
            distribution.put(rangeLabels[i], counts[i]);
        }
        
        // 计算各分数段占比
        Map<String, Double> percentages = new HashMap<>();
        for (int i = 0; i < rangeLabels.length; i++) {
            double percentage = totalCount > 0 ? Math.round(counts[i] * 10000.0 / totalCount) / 100.0 : 0.0;
            percentages.put(rangeLabels[i] + "_percent", percentage);
        }
        distribution.put("percentages", percentages);
        distribution.put("totalCount", totalCount);
        
        return distribution;
    }
}
