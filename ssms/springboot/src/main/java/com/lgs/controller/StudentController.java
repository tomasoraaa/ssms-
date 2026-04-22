package com.lgs.controller;

import com.github.pagehelper.PageInfo;
import com.lgs.common.Result;
import com.lgs.entity.Student;
import com.lgs.service.StudentService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;
    
    @Resource
    private ActivityLogService activityLogService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Student student) {
        try {
            studentService.add(student);
            activityLogService.recordLog("用户", "admin", "新增学生 " + student.getName(), "ADMIN");
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Student student) {
        try {
            studentService.updateById(student);
            activityLogService.recordLog("用户", "admin", "修改学生 " + student.getName() + " 的信息", "ADMIN");
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        // 先查询学生信息（通过id查询）
        Student student = new Student();
        student.setId(id);
        java.util.List<Student> students = studentService.selectAll(student);
        String studentName = "未知";
        if (students != null && !students.isEmpty()) {
            studentName = students.get(0).getName();
        }
        studentService.deleteById(id);
        activityLogService.recordLog("用户", "admin", "删除学生 " + studentName, "ADMIN");
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Student student,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Student> page = studentService.selectPage(student, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 批量导入
     */
    @PostMapping("/import")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        studentService.importExcel(file);
        activityLogService.recordLog("用户", "admin", "批量导入学生", "ADMIN");
        return Result.success();
    }

    /**
     * 根据用户名查询
     */
    @GetMapping("/selectByUsername")
    public Result selectByUsername(@RequestParam String username) {
        Student student = new Student();
        student.setUsername(username);
        return Result.success(studentService.selectAll(student).get(0));
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        Student student = new Student();
        return Result.success(studentService.selectAll(student));
    }
}
