package com.lgs.controller;

import com.github.pagehelper.PageInfo;
import com.lgs.common.Result;
import com.lgs.entity.StudentAdminClass;
import com.lgs.service.StudentAdminClassService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentAdminClass")
public class StudentAdminClassController {

    @Resource
    private StudentAdminClassService studentAdminClassService;

    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody StudentAdminClass studentAdminClass) {
        studentAdminClassService.add(studentAdminClass);
        return Result.success();
    }

    @PostMapping("/addBatch")
    public Result addBatch(@RequestBody List<StudentAdminClass> list) {
        studentAdminClassService.addBatch(list);
        activityLogService.recordLog("系统", "admin", "批量添加学生到行政班", "ADMIN");
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody StudentAdminClass studentAdminClass) {
        studentAdminClassService.updateById(studentAdminClass);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        studentAdminClassService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(StudentAdminClass studentAdminClass,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<StudentAdminClass> page = studentAdminClassService.selectPage(studentAdminClass, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectAll")
    public Result selectAll(StudentAdminClass studentAdminClass) {
        List<StudentAdminClass> list = studentAdminClassService.selectAll(studentAdminClass);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(studentAdminClassService.selectById(id));
    }

    @GetMapping("/selectByStudentId/{studentId}")
    public Result selectByStudentId(@PathVariable String studentId) {
        StudentAdminClass sac = new StudentAdminClass();
        sac.setStudent_id(studentId);
        List<StudentAdminClass> list = studentAdminClassService.selectAll(sac);
        return Result.success(list);
    }

    @DeleteMapping("/remove/{studentId}/{adminClassId}")
    public Result remove(@PathVariable String studentId, @PathVariable Integer adminClassId) {
        studentAdminClassService.deleteByStudentIdAndClassId(studentId, adminClassId);
        return Result.success();
    }
}