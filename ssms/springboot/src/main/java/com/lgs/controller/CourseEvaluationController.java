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

    // 提交或更新评价
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody CourseEvaluation courseEvaluation) {
        boolean success = courseEvaluationService.saveOrUpdate(courseEvaluation);
        if (success) {
            return Result.success("评价提交成功");
        } else {
            return Result.error("评价提交失败");
        }
    }

    // 根据学生ID和课程ID查询评价
    @GetMapping("/selectByStudentAndCourse")
    public Result selectByStudentAndCourse(@RequestParam String studentId, @RequestParam Integer courseId) {
        CourseEvaluation evaluation = courseEvaluationService.getByStudentAndCourse(studentId, courseId);
        return Result.success(evaluation);
    }

    // 根据课程ID查询评价列表
    @GetMapping("/selectByCourseId")
    public Result selectByCourseId(@RequestParam Integer courseId) {
        List<CourseEvaluation> evaluations = courseEvaluationService.getByCourseId(courseId);
        return Result.success(evaluations);
    }

    // 根据教师ID查询评价列表
    @GetMapping("/selectByTeacherId")
    public Result selectByTeacherId(@RequestParam String teacherId) {
        List<CourseEvaluation> evaluations = courseEvaluationService.getByTeacherId(teacherId);
        return Result.success(evaluations);
    }
    
    // 查询所有评价（管理员用）
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<CourseEvaluation> evaluations = courseEvaluationService.getAll();
        return Result.success(evaluations);
    }
    
    // 删除评价（管理员用）
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
