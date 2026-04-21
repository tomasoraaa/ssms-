package com.lgs.mapper;

import com.lgs.entity.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {

    void insert(Student student);
    @Select("select * from student where username=#{username} ")
    Student selectByUsername(String username);

    List<Student> selectAll(Student student);

    void updateById(Student student);

    /**
     * 删除
     */
    @Delete("delete from student where id = #{id}")
    void deleteById(Integer id);
    
    /**
     * 统计学生数量
     */
    @Select("select count(*) from student")
    int count();
}
