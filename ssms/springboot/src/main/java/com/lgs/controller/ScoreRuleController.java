package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.ScoreRule;
import com.lgs.service.ScoreRuleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scoreRule")
public class ScoreRuleController {

    @Resource
    private ScoreRuleService scoreRuleService;

    @PostMapping("/add")
    public Result add(@RequestBody ScoreRule scoreRule) {
        scoreRuleService.add(scoreRule);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(ScoreRule scoreRule) {
        return Result.success(scoreRuleService.selectAll(scoreRule));
    }

    @PutMapping("/update")
    public Result update(@RequestBody ScoreRule scoreRule) {
        scoreRuleService.updateById(scoreRule);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        scoreRuleService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(scoreRuleService.selectById(id));
    }

    @GetMapping("/selectByCourseId/{course_id}")
    public Result selectByCourseId(@PathVariable Integer course_id) {
        return Result.success(scoreRuleService.selectByCourseId(course_id));
    }
}