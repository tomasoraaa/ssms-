package com.lgs.mapper;

import com.lgs.entity.SystemConfig;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SystemConfigMapper {

    @Select("SELECT * FROM system_config WHERE config_key = #{config_key}")
    SystemConfig selectByKey(@Param("config_key") String config_key);

    @Select("SELECT * FROM system_config")
    List<SystemConfig> selectAll();

    @Insert("INSERT INTO system_config(config_key, config_value, description) VALUES(#{config_key}, #{config_value}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SystemConfig config);

    @Update("UPDATE system_config SET config_value = #{config_value} WHERE config_key = #{config_key}")
    int updateByKey(SystemConfig config);
}