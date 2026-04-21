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
    public void recordLog(String operateType, String operateUser, String description, String userType) {
        ActivityLog log = new ActivityLog();
        log.setOperateTime(LocalDateTime.now());
        log.setOperateType(operateType);
        log.setOperateUser(operateUser);
        log.setDescription(description);
        log.setUserType(userType);
        activityLogMapper.insert(log);
    }
    
    @Override
    public List<ActivityLog> getRecentLogs(int limit) {
        return activityLogMapper.selectRecent(limit);
    }
}
