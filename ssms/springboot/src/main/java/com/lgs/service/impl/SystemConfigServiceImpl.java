package com.lgs.service.impl;

import com.lgs.entity.SystemConfig;
import com.lgs.mapper.SystemConfigMapper;
import com.lgs.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public SystemConfig getByKey(String configKey) {
        return systemConfigMapper.selectByKey(configKey);
    }

    @Override
    public boolean isCourseSelectionEnabled() {
        SystemConfig config = systemConfigMapper.selectByKey("course_selection_enabled");
        return config != null && "true".equals(config.getConfigValue());
    }

    @Override
    public boolean isTeacherScoreEntryEnabled() {
        SystemConfig config = systemConfigMapper.selectByKey("teacher_score_entry_enabled");
        return config != null && "true".equals(config.getConfigValue());
    }

    @Override
    public boolean updateConfig(String configKey, String configValue) {
        SystemConfig config = systemConfigMapper.selectByKey(configKey);
        if (config == null) {
            config = new SystemConfig();
            config.setConfigKey(configKey);
            config.setConfigValue(configValue);
            return systemConfigMapper.insert(config) > 0;
        }
        config.setConfigValue(configValue);
        return systemConfigMapper.updateByKey(config) > 0;
    }
}