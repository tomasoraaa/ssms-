package com.lgs.service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 系统监控服务
 */
public interface SystemMonitorService {
    /**
     * 获取服务器状态
     */
    String getServerStatus();
    
    /**
     * 获取数据库状态
     */
    String getDatabaseStatus();
    
    /**
     * 获取系统版本
     */
    String getSystemVersion();
    
    /**
     * 获取最后更新时间
     */
    String getLastUpdateTime();
    
    /**
     * 更新系统状态
     */
    void updateSystemStatus();
}
