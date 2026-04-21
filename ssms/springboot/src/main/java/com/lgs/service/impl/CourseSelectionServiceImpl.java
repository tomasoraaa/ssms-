package com.lgs.service.impl;

import com.lgs.entity.CourseSelection;
import com.lgs.entity.StudentCourse;
import com.lgs.entity.TeachingClass;
import com.lgs.mapper.CourseSelectionMapper;
import com.lgs.mapper.StudentCourseMapper;
import com.lgs.mapper.TeachingClassMapper;
import com.lgs.service.CourseSelectionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Resource
    private CourseSelectionMapper courseSelectionMapper;

    @Resource
    private StudentCourseMapper studentCourseMapper;

    @Resource
    private TeachingClassMapper teachingClassMapper;

    @Override
    public void add(CourseSelection courseSelection) {
        courseSelectionMapper.insert(courseSelection);
    }

    @Override
    public List<CourseSelection> selectAll(CourseSelection courseSelection) {
        return courseSelectionMapper.selectAll(courseSelection);
    }

    @Override
    public void updateById(CourseSelection courseSelection) {
        courseSelectionMapper.updateById(courseSelection);
    }

    @Override
    public void deleteById(Integer id) {
        courseSelectionMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void approve(Integer id) {
        // 更新申请状态为已通过
        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setId(id);
        courseSelection.setStatus(1);
        courseSelectionMapper.updateById(courseSelection);

        // 查询申请详情
        CourseSelection selection = new CourseSelection();
        selection.setId(id);
        List<CourseSelection> selections = courseSelectionMapper.selectAll(selection);
        if (!selections.isEmpty()) {
            CourseSelection selected = selections.get(0);
            // 为学生添加课程关联
            if ("STUDENT".equals(selected.getUser_type())) {
                // 检查教学班容量
                if (selected.getTeaching_class_id() != null) {
                    TeachingClass teachingClass = teachingClassMapper.selectById(selected.getTeaching_class_id());
                    if (teachingClass != null) {
                        if (teachingClass.getSelected_count() >= teachingClass.getCapacity()) {
                            throw new RuntimeException("教学班容量已满");
                        }
                        // 更新教学班已选人数
                        teachingClass.setSelected_count(teachingClass.getSelected_count() + 1);
                        teachingClassMapper.updateById(teachingClass);
                    }
                }
                
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudent_id(selected.getUser_id());
                studentCourse.setCourse_id(selected.getCourse_id());
                studentCourse.setTeacher_id(selected.getTeacher_id());
                studentCourse.setTeacher_name(selected.getTeacher_name());
                studentCourse.setTeaching_class_id(selected.getTeaching_class_id());
                studentCourse.setStatus(1); // 1表示正常
                // 检查是否已存在关联
                List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
                if (existing.isEmpty()) {
                    studentCourseMapper.insert(studentCourse);
                }
            }
        }
    }

    @Override
    public void reject(Integer id) {
        // 更新申请状态为已拒绝
        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setId(id);
        courseSelection.setStatus(2);
        courseSelectionMapper.updateById(courseSelection);
    }
}