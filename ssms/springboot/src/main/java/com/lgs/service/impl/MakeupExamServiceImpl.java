package com.lgs.service.impl;

import com.lgs.entity.Course;
import com.lgs.entity.MakeupExam;
import com.lgs.entity.ScoreDetail;
import com.lgs.entity.ScoreRule;
import com.lgs.entity.StudentCourse;
import com.lgs.mapper.MakeupExamMapper;
import com.lgs.mapper.ScoreDetailMapper;
import com.lgs.service.CourseService;
import com.lgs.service.MakeupExamService;
import com.lgs.service.ScoreDetailService;
import com.lgs.service.ScoreRuleService;
import com.lgs.service.StudentCourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MakeupExamServiceImpl implements MakeupExamService {

    @Resource
    private MakeupExamMapper makeupExamMapper;

    @Resource
    private ScoreDetailMapper scoreDetailMapper;

    @Resource
    private StudentCourseService studentCourseService;

    @Resource
    private ScoreDetailService scoreDetailService;

    @Resource
    private ScoreRuleService scoreRuleService;

    @Resource
    private CourseService courseService;

    @Override
    public void add(MakeupExam makeupExam) {
        makeupExam.setStatus("待审批");
        makeupExamMapper.insert(makeupExam);
    }

    @Override
    public void updateById(MakeupExam makeupExam) {
        makeupExamMapper.updateById(makeupExam);
    }

    @Override
    public void deleteById(Integer id) {
        makeupExamMapper.deleteById(id);
    }

    @Override
    public MakeupExam selectById(Integer id) {
        return makeupExamMapper.selectById(id);
    }

    @Override
    public List<MakeupExam> selectAll(MakeupExam makeupExam) {
        return makeupExamMapper.selectAll(makeupExam);
    }

    @Override
    public List<MakeupExam> selectByStudentId(String student_id) {
        return makeupExamMapper.selectByStudentId(student_id);
    }

    @Override
    public List<MakeupExam> selectByCourseId(Integer course_id) {
        return makeupExamMapper.selectByCourseId(course_id);
    }

    @Override
    public List<MakeupExam> selectByTeachingClassId(Integer teaching_class_id) {
        return makeupExamMapper.selectByTeachingClassId(teaching_class_id);
    }

    @Override
    @Transactional
    public void approve(Integer id) {
        MakeupExam makeupExam = new MakeupExam();
        makeupExam.setId(id);
        makeupExam.setStatus("已通过");
        makeupExamMapper.updateById(makeupExam);
    }

    @Override
    @Transactional
    public void reject(Integer id) {
        MakeupExam makeupExam = new MakeupExam();
        makeupExam.setId(id);
        makeupExam.setStatus("已拒绝");
        makeupExamMapper.updateById(makeupExam);
    }

    @Override
    @Transactional
    public void updateScore(MakeupExam makeupExam) {
        MakeupExam existingExam = makeupExamMapper.selectById(makeupExam.getId());
        if (existingExam == null) {
            return;
        }

        String examType = makeupExam.getExam_type();
        Double makeupScore = makeupExam.getMakeup_score();
        Double originalScore = null;
        Double finalScore = null;
        Integer isFinal = 1;

        if ("缓考".equals(examType)) {
            isFinal = 0;
            ScoreDetail scoreDetail = scoreDetailService.selectByStudentAndCourse(
                    existingExam.getStudent_id(), existingExam.getCourse_id());
            if (scoreDetail != null) {
                originalScore = scoreDetail.getTotal_score();
                ScoreRule scoreRule = scoreRuleService.selectByCourseId(existingExam.getCourse_id());
                int usual_weight = 30, midterm_weight = 20, final_weight = 50;
                if (scoreRule != null) {
                    usual_weight = scoreRule.getUsual_weight();
                    midterm_weight = scoreRule.getMidterm_weight();
                    final_weight = scoreRule.getFinal_weight();
                }
                existingExam.setMakeup_score(makeupScore);
                makeupExamMapper.updateById(existingExam);
                double newTotalScore = scoreDetailService.calculateTotalScore(
                        scoreDetail.getUsual_score() != null ? scoreDetail.getUsual_score() : 0,
                        scoreDetail.getMidterm_score() != null ? scoreDetail.getMidterm_score() : 0,
                        makeupScore != null ? makeupScore : 0,
                        usual_weight, midterm_weight, final_weight
                );
                scoreDetail.setTotal_score(newTotalScore);
                scoreDetail.setFinal_score(makeupScore);
                scoreDetailMapper.updateById(scoreDetail);
                finalScore = newTotalScore;
            } else {
                originalScore = 0.0;
                ScoreRule scoreRule = scoreRuleService.selectByCourseId(existingExam.getCourse_id());
                int usual_weight = 30, midterm_weight = 20, final_weight = 50;
                if (scoreRule != null) {
                    usual_weight = scoreRule.getUsual_weight();
                    midterm_weight = scoreRule.getMidterm_weight();
                    final_weight = scoreRule.getFinal_weight();
                }
                double newTotalScore = scoreDetailService.calculateTotalScore(
                        0, 0, makeupScore != null ? makeupScore : 0,
                        usual_weight, midterm_weight, final_weight
                );
                ScoreDetail newScoreDetail = new ScoreDetail();
                newScoreDetail.setStudent_id(existingExam.getStudent_id());
                newScoreDetail.setCourse_id(existingExam.getCourse_id());
                newScoreDetail.setTeaching_class_id(existingExam.getTeaching_class_id());
                newScoreDetail.setFinal_score(makeupScore);
                newScoreDetail.setTotal_score(newTotalScore);
                scoreDetailMapper.insert(newScoreDetail);
                finalScore = newTotalScore;
            }
        } else {
            finalScore = makeupScore;
            // 保存补考成绩
            existingExam.setMakeup_score(makeupScore);
            makeupExamMapper.updateById(existingExam);
            StudentCourse studentCourse = studentCourseService.selectByStudentIdAndCourseId(
                    existingExam.getStudent_id(), existingExam.getCourse_id());
            if (studentCourse != null) {
                originalScore = studentCourse.getScore();
            }
            ScoreDetail scoreDetail = scoreDetailService.selectByStudentAndCourse(
                    existingExam.getStudent_id(), existingExam.getCourse_id());
            if (scoreDetail != null) {
                scoreDetail.setTotal_score(finalScore);
                scoreDetailMapper.updateById(scoreDetail);
            } else {
                ScoreDetail newScoreDetail = new ScoreDetail();
                newScoreDetail.setStudent_id(existingExam.getStudent_id());
                newScoreDetail.setCourse_id(existingExam.getCourse_id());
                newScoreDetail.setTeaching_class_id(existingExam.getTeaching_class_id());
                newScoreDetail.setTotal_score(finalScore);
                scoreDetailMapper.insert(newScoreDetail);
            }
        }

        makeupExam.setOriginal_score(originalScore);
        makeupExam.setIs_final(isFinal);
        makeupExamMapper.updateById(makeupExam);

        StudentCourse studentCourse = studentCourseService.selectByStudentIdAndCourseId(
                existingExam.getStudent_id(), existingExam.getCourse_id());
        if (studentCourse != null) {
            studentCourse.setScore(finalScore);
            if (makeupScore != null && makeupScore >= 60) {
                studentCourse.setIs_makeup(1);
                studentCourse.setOriginal_score(originalScore);
            }
            studentCourseService.updateScore(studentCourse);
        }
    }
}
