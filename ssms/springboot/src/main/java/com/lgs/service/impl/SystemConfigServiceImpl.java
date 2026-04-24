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
    public SystemConfig getByKey(String config_key) {
        return systemConfigMapper.selectByKey(config_key);
    }

    @Override
    public boolean isCourseSelectionEnabled() {
        SystemConfig config = systemConfigMapper.selectByKey("course_selection_enabled");
        return config != null && "true".equals(config.getConfig_value());
    }

    @Override
    public boolean isTeacherScoreEntryEnabled() {
        SystemConfig config = systemConfigMapper.selectByKey("teacher_score_entry_enabled");
        return config != null && "true".equals(config.getConfig_value());
    }

    @Override
    public boolean isMakeupExamScoreEntryEnabled() {
        SystemConfig config = systemConfigMapper.selectByKey("makeup_exam_score_entry_enabled");
        return config != null && "true".equals(config.getConfig_value());
    }

    @Override
    public boolean updateConfig(String config_key, String config_value) {
        SystemConfig config = systemConfigMapper.selectByKey(config_key);
        if (config == null) {
            config = new SystemConfig();
            config.setConfig_key(config_key);
            config.setConfig_value(config_value);
            return systemConfigMapper.insert(config) > 0;
        }
        config.setConfig_value(config_value);
        return systemConfigMapper.updateByKey(config) > 0;
    }
}