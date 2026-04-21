package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.service.SystemMonitorService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统监控控制器
 */
@RestController
@RequestMapping("/admin/system")
public class SystemController {
    
    @Resource
    private SystemMonitorService systemMonitorService;
    
    /**
     * 获取系统状态
     */
    @GetMapping("/getStatus")
    public Result getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("serverStatus", systemMonitorService.getServerStatus());
        status.put("databaseStatus", systemMonitorService.getDatabaseStatus());
        status.put("systemVersion", systemMonitorService.getSystemVersion());
        status.put("lastUpdate", systemMonitorService.getLastUpdateTime());
        return Result.success(status);
    }
    
    /**
     * 手动更新系统状态
     */
    @GetMapping("/updateStatus")
    public Result updateSystemStatus() {
        systemMonitorService.updateSystemStatus();
        return getSystemStatus();
    }
}
