package com.lgs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lgs.entity.Course;
import com.lgs.entity.CourseTeacher;
import com.lgs.entity.StudentCourse;
import com.lgs.entity.TeachingClass;
import com.lgs.mapper.CourseMapper;
import com.lgs.mapper.CourseTeacherMapper;
import com.lgs.mapper.StudentCourseMapper;
import com.lgs.service.AcademicYearService;
import com.lgs.service.CourseService;
import com.lgs.service.TeachingClassService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Resource
    private TeachingClassService teachingClassService;

    @Override
    public void add(Course course) {
        // 检查课程代码是否已存在
        Course existingCourse = courseMapper.selectByCourseCode(course.getCourse_code());
        if (existingCourse != null) {
            throw new RuntimeException("课程代码已存在，请重新输入");
        }
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
        // 检查课程代码是否已存在（排除当前记录）
        Course existingCourse = courseMapper.selectByCourseCode(course.getCourse_code());
        if (existingCourse != null && !existingCourse.getId().equals(course.getId())) {
            throw new RuntimeException("课程代码已存在，请重新输入");
        }
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
                    // 设置教学班ID
                    course.setTeaching_class_id(sc.getTeaching_class_id());
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

        // 再根据课程ID查询课程信息，使用Map去重
        Map<Integer, Course> courseMap = new HashMap<>();
        
        // 为每个课程加载信息和关联的教学班
        for (CourseTeacher ct : courseTeachers) {
            Integer courseId = ct.getCourse_id();
            
            Course course = courseMap.get(courseId);
            if (course == null) {
                course = courseMapper.selectById(courseId);
                if (course != null) {
                    courseMap.put(courseId, course);
                    
                    // 加载关联的教学班列表，并过滤出该教师教授的教学班
                    List<TeachingClass> allTeachingClasses = teachingClassService.selectByCourseId(courseId);
                    List<TeachingClass> teacherTeachingClasses = new ArrayList<>();
                    for (TeachingClass tc : allTeachingClasses) {
                        // 根据teaching_class表中的teacher_id字段来筛选
                        if (teacher_id.equals(tc.getTeacher_id())) {
                            teacherTeachingClasses.add(tc);
                        }
                    }
                    course.setTeachingClasses(teacherTeachingClasses);
                }
            }
        }
        
        // 将Map转换为List
        return new ArrayList<>(courseMap.values());
    }

    @Override
    public int count() {
        return courseMapper.count();
    }
}