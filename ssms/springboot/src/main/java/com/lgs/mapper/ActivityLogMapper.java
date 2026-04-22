package com.lgs.mapper;

import com.lgs.entity.ActivityLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 活动日志Mapper
 */
public interface ActivityLogMapper {
    /**
     * 插入活动日志
     */
    @Insert("insert into activity_log (operate_time, operate_type, operate_user, description, user_type) " +
            "values (#{operate_time}, #{operate_type}, #{operate_user}, #{description}, #{user_type})")
    void insert(ActivityLog activityLog);
    
    /**
     * 查询最近的活动日志
     */
    @Select("select * from activity_log order by operate_time desc limit #{limit}")
    List<ActivityLog> selectRecent(int limit);
}
