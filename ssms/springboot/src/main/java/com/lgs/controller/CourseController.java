package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.Course;
import com.lgs.exception.CustomException;
import com.lgs.service.AcademicYearService;
import com.lgs.service.CourseService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Resource
    private CourseService courseService;

    @Resource
    private AcademicYearService academicYearService;

    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody Course course) {
        try {
            courseService.add(course);
            activityLogService.recordLog("课程", "admin", "新增课程 \"" + course.getCourse_name() + "\"", "ADMIN");
            return Result.success();
        } catch (CustomException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            logger.error("新增课程异常", e);
            return Result.error("系统异常");
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody Course course) {
        try {
            courseService.updateById(course);
            activityLogService.recordLog("课程", "admin", "修改课程 \"" + course.getCourse_name() + "\" 的信息", "ADMIN");
            return Result.success();
        } catch (CustomException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            logger.error("修改课程异常", e);
            return Result.error("系统异常");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        Course course = courseService.selectById(id);
        courseService.deleteById(id);
        activityLogService.recordLog("课程", "admin", "删除课程 \"" + course.getCourse_name() + "\"", "ADMIN");
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(Course course, Integer pageNum, Integer pageSize) {
        return Result.success(courseService.selectPage(course, pageNum, pageSize));
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(courseService.selectById(id));
    }



    /**
     * 根据学生ID查询课程
     */
    @GetMapping("/selectByStudentId")
    public Result selectByStudentId(@RequestParam String student_id) {
        return Result.success(courseService.selectByStudentId(student_id));
    }

    /**
     * 根据教师ID查询课程
     */
    @GetMapping("/selectByTeacherId")
    public Result selectByTeacherId(@RequestParam String teacher_id) {
        return Result.success(courseService.selectByTeacherId(teacher_id));
    }

    /**
     * 查询所有课程
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(courseService.selectAll());
    }
}