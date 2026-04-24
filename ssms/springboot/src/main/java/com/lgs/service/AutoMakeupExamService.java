package com.lgs.service;

public interface AutoMakeupExamService {
    /**
     * 自动为成绩低于60分的学生提交补考申请并自动通过审核
     * @return 自动提交的补考申请数量
     */
    int autoSubmitMakeupExams();
}