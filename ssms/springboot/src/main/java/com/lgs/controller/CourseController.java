package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.Course;
import com.lgs.service.CourseService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;
    
    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody Course course) {
        courseService.add(course);
        activityLogService.recordLog("课程", "admin", "新增课程 \"" + course.getCourseName() + "\"", "ADMIN");
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(Course course, Integer pageNum, Integer pageSize) {
        return Result.success(courseService.selectPage(course, pageNum, pageSize));
    }

    @PutMapping("/update")
    public Result update(@RequestBody Course course) {
        courseService.updateById(course);
        activityLogService.recordLog("课程", "admin", "修改课程 \"" + course.getCourseName() + "\" 的信息", "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        // 先查询课程信息
        Course course = courseService.selectById(id);
        courseService.deleteById(id);
        if (course != null) {
            activityLogService.recordLog("课程", "admin", "删除课程 \"" + course.getCourseName() + "\"", "ADMIN");
        }
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(courseService.selectById(id));
    }



    /**
     * 根据学生ID查询课程
     */
    @GetMapping("/selectByStudentId")
    public Result selectByStudentId(@RequestParam String studentId) {
        return Result.success(courseService.selectByStudentId(studentId));
    }

    /**
     * 根据教师ID查询课程
     */
    @GetMapping("/selectByTeacherId")
    public Result selectByTeacherId(@RequestParam String teacherId) {
        return Result.success(courseService.selectByTeacherId(teacherId));
    }
}