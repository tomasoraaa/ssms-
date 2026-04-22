package com.lgs.service.impl;

import com.lgs.entity.ActivityLog;
import com.lgs.mapper.ActivityLogMapper;
import com.lgs.service.ActivityLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动日志服务实现类
 */
@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    
    @Resource
    private ActivityLogMapper activityLogMapper;
    
    @Override
    public void recordLog(String operate_type, String operate_user, String description, String user_type) {
        ActivityLog log = new ActivityLog();
        log.setOperate_time(LocalDateTime.now());
        log.setOperate_type(operate_type);
        log.setOperate_user(operate_user);  
        log.setDescription(description);
        log.setUser_type(user_type);    
        activityLogMapper.insert(log);
    }
    
    @Override
    public List<ActivityLog> getRecentLogs(int limit) {
        return activityLogMapper.selectRecent(limit);
    }
}
