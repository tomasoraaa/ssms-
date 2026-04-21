package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.Course;
import com.lgs.entity.CourseTeacher;
import com.lgs.entity.StudentCourse;
import com.lgs.mapper.CourseMapper;
import com.lgs.mapper.CourseTeacherMapper;
import com.lgs.mapper.StudentCourseMapper;
import com.lgs.service.AcademicYearService;
import com.lgs.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private StudentCourseMapper studentCourseMapper;

    @Resource
    private CourseTeacherMapper courseTeacherMapper;

    @Resource
    private AcademicYearService academicYearService;

    @Override
    public void add(Course course) {
        courseMapper.insert(course);
    }

    @Override
    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseMapper.selectAll(course);
        return PageInfo.of(list);
    }

    @Override
    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    @Override
    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }

    @Override
    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }


    @Override
    public List<Course> selectByStudentId(String student_id) {
        // 先查询学生关联的课程ID（只查询正常状态的课程）
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudent_id(student_id);
        List<StudentCourse> studentCourses = studentCourseMapper.selectAll(studentCourse);

        // 再根据课程ID查询课程信息，并设置学生选择的教师信息
        List<Course> courses = new ArrayList<>();
        for (StudentCourse sc : studentCourses) {
            // 只处理正常状态的课程
            if (sc.getStatus() == null || sc.getStatus() == 1) {
                Course course = courseMapper.selectById(Integer.parseInt(sc.getCourse_id()));
                if (course != null) {
                    // 设置学生选择的教师信息
                    course.setTeacher_name(sc.getTeacher_name());
                    course.setTeacher_id(sc.getTeacher_id());
                    // 设置成绩信息
                    course.setScore(sc.getScore());
                    // 设置修读学期信息
                    if (sc.getAcademic_year_id() != null) {
                        try {
                            com.lgs.entity.AcademicYear academicYear = academicYearService.selectById(sc.getAcademic_year_id());
                            if (academicYear != null) {
                                String semesterText = academicYear.getSemester() == 1 ? "第一学期" : "第二学期";
                                course.setAcademic_year_name(academicYear.getYear() + " " + semesterText);
                            }
                        } catch (Exception e) {
                            // 忽略异常，确保查询不中断
                        }
                    }
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    @Override
    public List<Course> selectByTeacherId(String teacher_id) {
        // 先查询教师关联的课程
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectByTeacherId(teacher_id);

        // 再根据课程ID查询课程信息
        List<Course> courses = new ArrayList<>();
        for (CourseTeacher ct : courseTeachers) {
            Course course = courseMapper.selectById(ct.getCourse_id());
            if (course != null) {
                courses.add(course);
            }
        }
        return courses;
    }

    @Override
    public int count() {
        return courseMapper.count();
    }
}