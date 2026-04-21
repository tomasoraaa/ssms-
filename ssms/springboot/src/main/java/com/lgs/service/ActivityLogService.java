package com.lgs.service;

import com.lgs.entity.ActivityLog;

import java.util.List;

/**
 * 活动日志服务接口
 */
public interface ActivityLogService {
    /**
     * 记录活动日志
     */
    void recordLog(String operateType, String operateUser, String description, String userType);
    
    /**
     * 获取最近的活动日志
     */
    List<ActivityLog> getRecentLogs(int limit);
}
