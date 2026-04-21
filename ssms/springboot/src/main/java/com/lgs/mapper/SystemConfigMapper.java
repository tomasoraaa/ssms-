package com.lgs.mapper;

import com.lgs.entity.SystemConfig;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SystemConfigMapper {

    @Select("SELECT * FROM system_config WHERE config_key = #{configKey}")
    SystemConfig selectByKey(@Param("configKey") String configKey);

    @Select("SELECT * FROM system_config")
    List<SystemConfig> selectAll();

    @Insert("INSERT INTO system_config(config_key, config_value, description) VALUES(#{configKey}, #{configValue}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SystemConfig config);

    @Update("UPDATE system_config SET config_value = #{configValue} WHERE config_key = #{configKey}")
    int updateByKey(SystemConfig config);
}