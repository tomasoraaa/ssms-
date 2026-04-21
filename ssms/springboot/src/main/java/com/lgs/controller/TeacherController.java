package com.lgs.controller;

import com.github.pagehelper.PageInfo;
import com.lgs.common.Result;
import com.lgs.entity.Teacher;
import com.lgs.service.TeacherService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private TeacherService teacherService;
    
    @Resource
    private ActivityLogService activityLogService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Teacher teacher) {
        teacherService.add(teacher);
        activityLogService.recordLog("用户", "admin", "新增教师 " + teacher.getName(), "ADMIN");
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        activityLogService.recordLog("用户", "admin", "修改教师 " + teacher.getName() + " 的信息", "ADMIN");
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        // 先查询教师信息（通过id查询）
        Teacher teacher = new Teacher();
        teacher.setId(id);
        java.util.List<Teacher> teachers = teacherService.selectAll(teacher);
        String teacherName = "未知";
        if (teachers != null && !teachers.isEmpty()) {
            teacherName = teachers.get(0).getName();
        }
        teacherService.deleteById(id);
        activityLogService.recordLog("用户", "admin", "删除教师 " + teacherName, "ADMIN");
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Teacher teacher,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Teacher> page = teacherService.selectPage(teacher, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 批量导入
     */
    @PostMapping("/import")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        teacherService.importExcel(file);
        activityLogService.recordLog("用户", "admin", "批量导入教师", "ADMIN");
        return Result.success();
    }

    /**
     * 获取所有教师信息
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(teacherService.selectAll(new Teacher()));
    }

    /**
     * 根据用户名查询
     */
    @GetMapping("/selectByUsername")
    public Result selectByUsername(@RequestParam String username) {
        Teacher teacher = new Teacher();
        teacher.setUsername(username);
        return Result.success(teacherService.selectAll(teacher).get(0));
    }
}
