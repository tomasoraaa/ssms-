package com.lgs.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动日志实体类
 */
@Data
public class ActivityLog {
    /**
     * 日志ID
     */
    private Integer id;
    
    /**
     * 操作时间
     */
    private LocalDateTime operateTime;
    
    /**
     * 操作类型
     */
    private String operateType;
    
    /**
     * 操作用户
     */
    private String operateUser;
    
    /**
     * 操作描述
     */
    private String description;
    
    /**
     * 用户类型
     */
    private String userType;
}
