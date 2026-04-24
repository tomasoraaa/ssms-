package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.MakeupExam;
import com.lgs.service.MakeupExamService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/makeupExam")
public class MakeupExamController {

    @Resource
    private MakeupExamService makeupExamService;

    @PostMapping("/add")
    public Result add(@RequestBody MakeupExam makeupExam) {
        makeupExamService.add(makeupExam);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(MakeupExam makeupExam) {
        return Result.success(makeupExamService.selectAll(makeupExam));
    }

    @PutMapping("/update")
    public Result update(@RequestBody MakeupExam makeupExam) {
        makeupExamService.updateById(makeupExam);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        makeupExamService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(makeupExamService.selectById(id));
    }

    @GetMapping("/selectByStudentId/{student_id}")
    public Result selectByStudentId(@PathVariable String student_id) {
        return Result.success(makeupExamService.selectByStudentId(student_id));
    }

    @GetMapping("/selectByCourseId/{course_id}")
    public Result selectByCourseId(@PathVariable Integer course_id) {
        return Result.success(makeupExamService.selectByCourseId(course_id));
    }

    @GetMapping("/selectByTeachingClassId/{teaching_class_id}")
    public Result selectByTeachingClassId(@PathVariable Integer teaching_class_id) {
        return Result.success(makeupExamService.selectByTeachingClassId(teaching_class_id));
    }

    @PutMapping("/approve/{id}")
    public Result approve(@PathVariable Integer id) {
        makeupExamService.approve(id);
        return Result.success();
    }

    @PutMapping("/reject/{id}")
    public Result reject(@PathVariable Integer id) {
        makeupExamService.reject(id);
        return Result.success();
    }

    @PutMapping("/updateScore")
    public Result updateScore(@RequestBody MakeupExam makeupExam) {
        makeupExamService.updateScore(makeupExam);
        return Result.success();
    }
}