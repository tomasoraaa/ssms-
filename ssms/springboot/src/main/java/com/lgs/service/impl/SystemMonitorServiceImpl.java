package com.lgs.service.impl;

import com.lgs.service.SystemMonitorService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统监控服务实现类
 */
@Service
public class SystemMonitorServiceImpl implements SystemMonitorService {
    // 注入数据源
    @Resource
    private DataSource dataSource;
    
    // 系统版本
    private static final String SYSTEM_VERSION = "1.0.0";
    
    // 最后更新时间
    private String lastUpdateTime;
    
    // 服务器状态
    private String serverStatus;
    
    // 数据库状态
    private String databaseStatus;
    
    public SystemMonitorServiceImpl() {
        // 构造函数中不再调用updateSystemStatus()
    }
    
    // 使用@PostConstruct注解，在依赖注入完成后初始化
    @PostConstruct
    public void init() {
        updateSystemStatus();
    }
    
    @Override
    public String getServerStatus() {
        return serverStatus;
    }
    
    @Override
    public String getDatabaseStatus() {
        return databaseStatus;
    }
    
    @Override
    public String getSystemVersion() {
        return SYSTEM_VERSION;
    }
    
    @Override
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    @Override
    public void updateSystemStatus() {
        // 更新服务器状态
        updateServerStatus();
        
        // 更新数据库状态
        updateDatabaseStatus();
        
        // 更新最后更新时间
        lastUpdateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    
    /**
     * 更新服务器状态
     */
    private void updateServerStatus() {
        try {
            // 检查服务器CPU和内存状态
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            
            // 检查磁盘空间
            // 针对Windows系统，使用系统驱动器
            String systemDrive = System.getenv("SystemDrive");
            if (systemDrive == null) {
                systemDrive = "/";
            }
            File root = new File(systemDrive);
            long totalSpace = root.getTotalSpace();
            long freeSpace = root.getFreeSpace();
            double freeSpacePercent = (double) freeSpace / totalSpace * 100;
            
            // 如果磁盘空间充足，服务器状态为在线
            if (freeSpacePercent > 0) {
                serverStatus = "在线";
            } else {
                serverStatus = "警告";
            }
        } catch (Exception e) {
            serverStatus = "异常";
        }
    }
    
    /**
     * 更新数据库状态
     */
    private void updateDatabaseStatus() {
        Connection connection = null;
        try {
            // 使用Spring Boot的DataSource获取连接
            if (dataSource != null) {
                connection = dataSource.getConnection();
                if (connection != null && !connection.isClosed()) {
                    databaseStatus = "正常";
                } else {
                    databaseStatus = "异常";
                }
            } else {
                databaseStatus = "异常";
            }
        } catch (SQLException e) {
            databaseStatus = "异常";
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // 忽略关闭连接的异常
                }
            }
        }
    }
}
