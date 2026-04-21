package com.lgs.controller;

import com.github.pagehelper.PageInfo;
import com.lgs.common.Result;
import com.lgs.entity.AcademicYear;
import com.lgs.service.AcademicYearService;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academicYear")
public class AcademicYearController {

    @Resource
    private AcademicYearService academicYearService;

    @Resource
    private ActivityLogService activityLogService;

    @PostMapping("/add")
    public Result add(@RequestBody AcademicYear academicYear) {
        academicYearService.add(academicYear);
        activityLogService.recordLog("系统", "admin", "新增学年学期 " + academicYear.getYear() + " 第" + academicYear.getSemester() + "学期", "ADMIN");
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody AcademicYear academicYear) {
        academicYearService.updateById(academicYear);
        activityLogService.recordLog("系统", "admin", "修改学年学期 " + academicYear.getYear() + " 第" + academicYear.getSemester() + "学期", "ADMIN");
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        AcademicYear academicYear = academicYearService.selectById(id);
        academicYearService.deleteById(id);
        if (academicYear != null) {
            activityLogService.recordLog("系统", "admin", "删除学年学期 " + academicYear.getYear() + " 第" + academicYear.getSemester() + "学期", "ADMIN");
        }
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(AcademicYear academicYear,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<AcademicYear> page = academicYearService.selectPage(academicYear, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectAll")
    public Result selectAll(AcademicYear academicYear) {
        List<AcademicYear> list = academicYearService.selectAll(academicYear);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(academicYearService.selectById(id));
    }

    @GetMapping("/selectCurrent")
    public Result selectCurrent() {
        return Result.success(academicYearService.selectCurrent());
    }
}
