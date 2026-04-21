package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.CourseEvaluation;
import com.lgs.service.CourseEvaluationService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/courseEvaluation")
public class CourseEvaluationController {

    @Resource
    private CourseEvaluationService courseEvaluationService;

    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody CourseEvaluation courseEvaluation) {
        boolean success = courseEvaluationService.saveOrUpdate(courseEvaluation);
        if (success) {
            return Result.success("评价提交成功");
        } else {
            return Result.error("评价提交失败");
        }
    }

    @GetMapping("/selectByStudentAndCourse")
    public Result selectByStudentAndCourse(@RequestParam String student_id, @RequestParam Integer course_id) {
        CourseEvaluation evaluation = courseEvaluationService.getByStudentAndCourse(student_id, course_id);
        return Result.success(evaluation);
    }

    @GetMapping("/selectByCourseId")
    public Result selectByCourseId(@RequestParam Integer course_id) {
        List<CourseEvaluation> evaluations = courseEvaluationService.getByCourseId(course_id);
        return Result.success(evaluations);
    }

    @GetMapping("/selectByTeacherId")
    public Result selectByTeacherId(@RequestParam String teacher_id) {
        List<CourseEvaluation> evaluations = courseEvaluationService.getByTeacherId(teacher_id);
        return Result.success(evaluations);
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<CourseEvaluation> evaluations = courseEvaluationService.getAll();
        return Result.success(evaluations);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        boolean success = courseEvaluationService.deleteById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}