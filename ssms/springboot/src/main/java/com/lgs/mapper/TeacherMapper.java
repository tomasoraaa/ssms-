package com.lgs.mapper;

import com.lgs.entity.Teacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {

    void insert(Teacher teacher);
    @Select("select * from teacher where username=#{username} ")
    Teacher selectByUsername(String username);

    List<Teacher> selectAll(Teacher teacher);

    void updateById(Teacher teacher);

    /**
     * 删除
     */
    @Delete("delete from teacher where id = #{id}")
    void deleteById(Integer id);
    
    /**
     * 统计教师数量
     */
    @Select("select count(*) from teacher")
    int count();
}
