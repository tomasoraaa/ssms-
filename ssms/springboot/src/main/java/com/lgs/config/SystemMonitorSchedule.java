package com.lgs.config;

import com.lgs.service.SystemMonitorService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 系统监控定时任务
 */
@Configuration
@EnableScheduling
public class SystemMonitorSchedule {
    
    @Resource
    private SystemMonitorService systemMonitorService;
    
    /**
     * 每分钟更新一次系统状态
     */
    @Scheduled(fixedRate = 60000) // 60秒
    public void updateSystemStatus() {
        systemMonitorService.updateSystemStatus();
    }
}
