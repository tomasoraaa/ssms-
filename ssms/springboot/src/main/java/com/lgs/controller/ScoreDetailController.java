package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.ScoreDetail;
import com.lgs.service.ScoreDetailService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scoreDetail")
public class ScoreDetailController {

    @Resource
    private ScoreDetailService scoreDetailService;

    @PostMapping("/add")
    public Result add(@RequestBody ScoreDetail scoreDetail) {
        scoreDetailService.add(scoreDetail);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(ScoreDetail scoreDetail) {
        return Result.success(scoreDetailService.selectAll(scoreDetail));
    }

    @PutMapping("/update")
    public Result update(@RequestBody ScoreDetail scoreDetail) {
        scoreDetailService.updateById(scoreDetail);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        scoreDetailService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(scoreDetailService.selectById(id));
    }

    @GetMapping("/selectByStudentAndCourse")
    public Result selectByStudentAndCourse(@RequestParam String student_id, @RequestParam Integer course_id) {
        return Result.success(scoreDetailService.selectByStudentAndCourse(student_id, course_id));
    }

    @GetMapping("/selectByStudentAndTeachingClass")
    public Result selectByStudentAndTeachingClass(@RequestParam String student_id, @RequestParam Integer teaching_class_id) {
        return Result.success(scoreDetailService.selectByStudentAndTeachingClass(student_id, teaching_class_id));
    }

    @GetMapping("/selectByCourseId/{course_id}")
    public Result selectByCourseId(@PathVariable Integer course_id) {
        return Result.success(scoreDetailService.selectByCourseId(course_id));
    }

    @GetMapping("/selectByTeachingClassId/{teaching_class_id}")
    public Result selectByTeachingClassId(@PathVariable Integer teaching_class_id) {
        return Result.success(scoreDetailService.selectByTeachingClassId(teaching_class_id));
    }

    @GetMapping("/selectByStudentId/{student_id}")
    public Result selectByStudentId(@PathVariable String student_id) {
        return Result.success(scoreDetailService.selectByStudentId(student_id));
    }
    
    @GetMapping("/selectByStudentId")
    public Result selectByStudentId(
            @RequestParam String studentId,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer academicYearId) {
        return Result.success(scoreDetailService.selectByStudentIdWithParams(studentId, courseId, academicYearId));
    }
    
    @GetMapping("/selectByStudentIdWithParams")
    public Result selectByStudentIdWithParams(
            @RequestParam String studentId,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer academicYearId) {
        return Result.success(scoreDetailService.selectByStudentIdWithParams(studentId, courseId, academicYearId));
    }

    @GetMapping("/getWarningStudents/{teaching_class_id}")
    public Result getWarningStudents(@PathVariable Integer teaching_class_id) {
        return Result.success(scoreDetailService.getWarningStudents(teaching_class_id));
    }

    @GetMapping("/calculateTotalScore")
    public Result calculateTotalScore(
            @RequestParam double usual_score,
            @RequestParam double midterm_score,
            @RequestParam double final_score,
            @RequestParam Integer usual_weight,
            @RequestParam Integer midterm_weight,
            @RequestParam Integer final_weight) {
        double total_score = scoreDetailService.calculateTotalScore(usual_score, midterm_score, final_score, usual_weight, midterm_weight, final_weight);
        return Result.success(total_score);
    }

    /**
     * 获取班级成绩统计（平均分、最高分、最低分）
     */
    @GetMapping("/getClassStatistics/{teaching_class_id}")
    public Result getClassScoreStatistics(@PathVariable Integer teaching_class_id) {
        return Result.success(scoreDetailService.getClassScoreStatistics(teaching_class_id));
    }

    /**
     * 获取成绩分布统计（各分数段人数）
     */
    @GetMapping("/getScoreDistribution/{teaching_class_id}")
    public Result getScoreDistribution(@PathVariable Integer teaching_class_id) {
        return Result.success(scoreDetailService.getScoreDistribution(teaching_class_id));
    }
}