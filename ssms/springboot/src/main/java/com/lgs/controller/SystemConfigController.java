package com.lgs.controller;

import com.lgs.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/systemConfig")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping("/isCourseSelectionEnabled")
    public Map<String, Object> isCourseSelectionEnabled() {
        Map<String, Object> result = new HashMap<>();
        boolean enabled = systemConfigService.isCourseSelectionEnabled();
        result.put("code", "200");
        result.put("data", enabled);
        result.put("enabled", enabled);
        return result;
    }

    @PostMapping("/setCourseSelectionEnabled/{enabled}")
    public Map<String, Object> setCourseSelectionEnabled(@PathVariable boolean enabled) {
        Map<String, Object> result = new HashMap<>();
        boolean success = systemConfigService.updateConfig("course_selection_enabled", String.valueOf(enabled));
        result.put("code", success ? "200" : "500");
        result.put("msg", success ? "操作成功" : "操作失败");
        return result;
    }

    @GetMapping("/isTeacherScoreEntryEnabled")
    public Map<String, Object> isTeacherScoreEntryEnabled() {
        Map<String, Object> result = new HashMap<>();
        boolean enabled = systemConfigService.isTeacherScoreEntryEnabled();
        result.put("code", "200");
        result.put("data", enabled);
        result.put("enabled", enabled);
        return result;
    }

    @PostMapping("/setTeacherScoreEntryEnabled/{enabled}")
    public Map<String, Object> setTeacherScoreEntryEnabled(@PathVariable boolean enabled) {
        Map<String, Object> result = new HashMap<>();
        boolean success = systemConfigService.updateConfig("teacher_score_entry_enabled", String.valueOf(enabled));
        result.put("code", success ? "200" : "500");
        result.put("msg", success ? "操作成功" : "操作失败");
        return result;
    }
}