package com.lgs.service;

import com.lgs.entity.SystemConfig;

public interface SystemConfigService {
    SystemConfig getByKey(String configKey);
    boolean isCourseSelectionEnabled();
    boolean isTeacherScoreEntryEnabled();
    boolean updateConfig(String configKey, String configValue);
}