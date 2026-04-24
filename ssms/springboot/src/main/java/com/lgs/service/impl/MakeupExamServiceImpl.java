package com.lgs.service.impl;

import com.lgs.entity.MakeupExam;
import com.lgs.mapper.MakeupExamMapper;
import com.lgs.service.MakeupExamService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MakeupExamServiceImpl implements MakeupExamService {

    @Resource
    private MakeupExamMapper makeupExamMapper;

    @Override
    public void add(MakeupExam makeupExam) {
        makeupExam.setStatus("待审批");
        makeupExamMapper.insert(makeupExam);
    }

    @Override
    public void updateById(MakeupExam makeupExam) {
        makeupExamMapper.updateById(makeupExam);
    }

    @Override
    public void deleteById(Integer id) {
        makeupExamMapper.deleteById(id);
    }

    @Override
    public MakeupExam selectById(Integer id) {
        return makeupExamMapper.selectById(id);
    }

    @Override
    public List<MakeupExam> selectAll(MakeupExam makeupExam) {
        return makeupExamMapper.selectAll(makeupExam);
    }

    @Override
    public List<MakeupExam> selectByStudentId(String student_id) {
        return makeupExamMapper.selectByStudentId(student_id);
    }

    @Override
    public List<MakeupExam> selectByCourseId(Integer course_id) {
        return makeupExamMapper.selectByCourseId(course_id);
    }

    @Override
    public List<MakeupExam> selectByTeachingClassId(Integer teaching_class_id) {
        return makeupExamMapper.selectByTeachingClassId(teaching_class_id);
    }

    @Override
    @Transactional
    public void approve(Integer id) {
        MakeupExam makeupExam = new MakeupExam();
        makeupExam.setId(id);
        makeupExam.setStatus("已通过");
        makeupExamMapper.updateById(makeupExam);
    }

    @Override
    @Transactional
    public void reject(Integer id) {
        MakeupExam makeupExam = new MakeupExam();
        makeupExam.setId(id);
        makeupExam.setStatus("已拒绝");
        makeupExamMapper.updateById(makeupExam);
    }
}