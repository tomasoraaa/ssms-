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
        if (courseSelection.getTeaching_class_id() == null) {
            throw new RuntimeException("请选择教学班");
        }

        TeachingClass teachingClass = teachingClassMapper.selectById(courseSelection.getTeaching_class_id());
        if (teachingClass == null) {
            throw new RuntimeException("教学班不存在");
        }

        if (teachingClass.getSelected_count() >= teachingClass.getCapacity()) {
            throw new RuntimeException("教学班容量已满");
        }

        if ("STUDENT".equals(courseSelection.getUser_type())) {
            List<StudentCourse> existingCourses = studentCourseMapper.selectByStudentId(courseSelection.getUser_id());
            for (StudentCourse sc : existingCourses) {
                if (sc.getTeaching_class_id() != null) {
                    TeachingClass existingTc = teachingClassMapper.selectById(sc.getTeaching_class_id());
                    if (existingTc != null && teachingClass.hasTimeConflict(existingTc)) {
                        throw new RuntimeException("时间冲突: " + existingTc.getClass_code() + " (" + existingTc.getScheduleText() + ")");
                    }
                }
            }

            List<StudentCourse> existing = studentCourseMapper.selectAll(new StudentCourse());
            for (StudentCourse sc : existing) {
                if (sc.getStudent_id().equals(courseSelection.getUser_id()) &&
                    sc.getCourse_id().equals(courseSelection.getCourse_id())) {
                    throw new RuntimeException("已选过该课程");
                }
            }

            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setStudent_id(courseSelection.getUser_id());
            studentCourse.setCourse_id(courseSelection.getCourse_id());
            studentCourse.setTeacher_id(courseSelection.getTeacher_id());
            studentCourse.setTeacher_name(courseSelection.getTeacher_name());
            studentCourse.setTeaching_class_id(courseSelection.getTeaching_class_id());
            studentCourse.setAcademic_year_id(teachingClass.getAcademic_year_id());
            studentCourse.setStatus(1);
            studentCourseMapper.insert(studentCourse);

            teachingClass.setSelected_count(teachingClass.getSelected_count() + 1);
            teachingClassMapper.updateById(teachingClass);
        }

        courseSelection.setStatus(1);
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
        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setId(id);
        courseSelection.setStatus(1);
        courseSelectionMapper.updateById(courseSelection);

        CourseSelection selection = new CourseSelection();
        selection.setId(id);
        List<CourseSelection> selections = courseSelectionMapper.selectAll(selection);
        if (!selections.isEmpty()) {
            CourseSelection selected = selections.get(0);
            if ("STUDENT".equals(selected.getUser_type())) {
                if (selected.getTeaching_class_id() != null) {
                    TeachingClass teachingClass = teachingClassMapper.selectById(selected.getTeaching_class_id());
                    if (teachingClass != null) {
                        if (teachingClass.getSelected_count() >= teachingClass.getCapacity()) {
                            throw new RuntimeException("教学班容量已满");
                        }
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
                studentCourse.setStatus(1);
                List<StudentCourse> existing = studentCourseMapper.selectAll(studentCourse);
                if (existing.isEmpty()) {
                    studentCourseMapper.insert(studentCourse);
                }
            }
        }
    }

    @Override
    public void reject(Integer id) {
        CourseSelection courseSelection = new CourseSelection();
        courseSelection.setId(id);
        courseSelection.setStatus(2);
        courseSelectionMapper.updateById(courseSelection);
    }
}
