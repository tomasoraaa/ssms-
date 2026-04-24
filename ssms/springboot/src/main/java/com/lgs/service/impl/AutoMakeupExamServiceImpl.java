package com.lgs.service.impl;

import com.lgs.entity.MakeupExam;
import com.lgs.entity.StudentCourse;
import com.lgs.service.AutoMakeupExamService;
import com.lgs.service.MakeupExamService;
import com.lgs.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoMakeupExamServiceImpl implements AutoMakeupExamService {

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private MakeupExamService makeupExamService;

    @Override
    public int autoSubmitMakeupExams() {
        int count = 0;

        // 1. 查询所有成绩低于60分的学生课程记录
        List<StudentCourse> lowScoreCourses = studentCourseService.selectLowScoreCourses(60.0);

        // 2. 对每个记录，检查是否已经有补考申请
        for (StudentCourse course : lowScoreCourses) {
            // 跳过无效记录
            if (course.getStudent_id() == null || course.getCourse_id() == null) {
                continue;
            }

            // 检查course_id是否为有效的数字
            Integer courseIdInt;
            try {
                courseIdInt = Integer.valueOf(course.getCourse_id());
            } catch (NumberFormatException e) {
                // 跳过无效的course_id
                continue;
            }

            // 检查是否已经有补考申请
            MakeupExam query = new MakeupExam();
            query.setStudent_id(course.getStudent_id());
            query.setCourse_id(courseIdInt);
            List<MakeupExam> existingExams = makeupExamService.selectAll(query);

            // 如果没有补考申请，创建新的补考申请
            if (existingExams == null || existingExams.isEmpty()) {
                MakeupExam makeupExam = new MakeupExam();
                makeupExam.setStudent_id(course.getStudent_id());
                makeupExam.setCourse_id(courseIdInt);
                makeupExam.setTeaching_class_id(course.getTeaching_class_id());
                makeupExam.setExam_type("补考");
                makeupExam.setOriginal_score(course.getScore());
                makeupExam.setStatus("approved"); // 自动通过审核
                makeupExam.setReason("系统自动提交：成绩低于60分");

                // 保存补考申请
                makeupExamService.add(makeupExam);
                count++;

                // 3. 更新学生课程的is_makeup字段
                course.setIs_makeup(1);
                studentCourseService.updateById(course);
            }
        }

        return count;
    }
}