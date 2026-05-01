package com.lgs.controller;

import com.lgs.entity.ScoreDetail;
import com.lgs.entity.Student;
import com.lgs.service.ScoreDetailService;
import com.lgs.service.StudentService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 成绩导出控制器
 */
@RestController
@RequestMapping("/scoreExport")
public class ScoreExportController {

    @Resource
    private ScoreDetailService scoreDetailService;

    @Resource
    private StudentService studentService;

    /**
     * 教师导出教学班成绩
     */
    @GetMapping("/teacher")
    public void exportTeacherScores(
            @RequestParam Integer teaching_class_id,
            @RequestParam(required = false) Integer course_id,
            HttpServletResponse response) throws Exception {
        
        // 查询教学班成绩详情
        ScoreDetail query = new ScoreDetail();
        query.setTeaching_class_id(teaching_class_id);
        if (course_id != null) {
            query.setCourse_id(course_id);
        }
        List<ScoreDetail> scoreDetails = scoreDetailService.selectAll(query);
        
        // 检查是否有成绩数据
        if (scoreDetails == null || scoreDetails.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无成绩数据可导出");
            return;
        }
        
        // 按需查询学生信息（仅查询有成绩记录的学生）
        Map<String, Student> studentMap = getStudentMap(scoreDetails);
        
        // 生成Excel
        try (Workbook workbook = generateScoreExcel(scoreDetails, studentMap);
             OutputStream outputStream = response.getOutputStream()) {
            
            // 设置响应头
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("教学班成绩.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + fileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // 写入响应
            workbook.write(outputStream);
            outputStream.flush();
        }
    }

    /**
     * 学生导出个人成绩
     */
    @GetMapping("/student")
    public void exportStudentScores(
            @RequestParam String student_id,
            HttpServletResponse response) throws Exception {
        
        // 查询学生成绩详情
        ScoreDetail query = new ScoreDetail();
        query.setStudent_id(student_id);
        List<ScoreDetail> scoreDetails = scoreDetailService.selectAll(query);
        
        // 检查是否有成绩数据
        if (scoreDetails == null || scoreDetails.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无成绩数据可导出");
            return;
        }
        
        // 按需查询学生信息（仅查询当前学生）
        Map<String, Student> studentMap = getStudentMap(scoreDetails);
        
        // 生成Excel
        try (Workbook workbook = generateScoreExcel(scoreDetails, studentMap);
             OutputStream outputStream = response.getOutputStream()) {
            
            // 设置响应头
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("个人成绩.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + fileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // 写入响应
            workbook.write(outputStream);
            outputStream.flush();
        }
    }

    /**
     * 管理员导出教学班成绩
     */
    @GetMapping("/admin")
    public void exportAdminScores(
            @RequestParam Integer teaching_class_id,
            HttpServletResponse response) throws Exception {
        
        // 查询教学班成绩详情
        ScoreDetail query = new ScoreDetail();
        query.setTeaching_class_id(teaching_class_id);
        List<ScoreDetail> scoreDetails = scoreDetailService.selectAll(query);
        
        // 检查是否有成绩数据
        if (scoreDetails == null || scoreDetails.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无成绩数据可导出");
            return;
        }
        
        // 按需查询学生信息（仅查询有成绩记录的学生）
        Map<String, Student> studentMap = getStudentMap(scoreDetails);
        
        // 生成Excel
        try (Workbook workbook = generateScoreExcel(scoreDetails, studentMap);
             OutputStream outputStream = response.getOutputStream()) {
            
            // 设置响应头
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("教学班成绩.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + fileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // 写入响应
            workbook.write(outputStream);
            outputStream.flush();
        }
    }

    /**
     * 按需获取学生信息Map
     * 从成绩详情中提取学生ID，仅查询需要的学生信息
     */
    private Map<String, Student> getStudentMap(List<ScoreDetail> scoreDetails) {
        Map<String, Student> studentMap = new HashMap<>();
        
        if (scoreDetails == null || scoreDetails.isEmpty()) {
            return studentMap;
        }
        
        // 提取需要查询的学生ID列表
        List<String> studentIds = scoreDetails.stream()
                .map(ScoreDetail::getStudent_id)
                .filter(id -> id != null && !id.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        
        if (studentIds.isEmpty()) {
            return studentMap;
        }
        
        // 按需查询学生信息
        List<Student> students = studentService.selectByUsernames(studentIds);
        
        // 转换为Map，便于快速查找
        for (Student student : students) {
            studentMap.put(student.getUsername(), student);
        }
        
        return studentMap;
    }

    /**
     * 生成成绩Excel文件
     */
    private Workbook generateScoreExcel(List<ScoreDetail> scoreDetails, Map<String, Student> studentMap) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("成绩表");
        
        // 创建标题行样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        // 创建数据行样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        
        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {"学号", "姓名", "平时成绩", "期中成绩", "期末成绩", "总评成绩", "成绩来源", "原始成绩", "绩点", "等级", "状态"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充数据
        int rowNum = 1;
        for (ScoreDetail detail : scoreDetails) {
            Row row = sheet.createRow(rowNum++);
            
            // 查找学生信息（O(1)复杂度）
            Student student = studentMap.get(detail.getStudent_id());
            
            // 学号
            createCell(row, 0, detail.getStudent_id(), dataStyle);
            // 姓名
            createCell(row, 1, student != null ? student.getName() : "-", dataStyle);
            // 平时成绩
            createCell(row, 2, formatScore(detail.getUsual_score()), dataStyle);
            // 期中成绩
            createCell(row, 3, formatScore(detail.getMidterm_score()), dataStyle);
            // 期末成绩
            createCell(row, 4, formatScore(detail.getFinal_score()), dataStyle);
            // 总评成绩
            createCell(row, 5, formatScore(detail.getTotal_score()), dataStyle);
            // 成绩来源
            String source = detail.getIs_makeup() != null && detail.getIs_makeup() == 1 
                    ? ("补考".equals(detail.getMakeup_exam_type()) ? "补考" : "缓考")
                    : "正常";
            createCell(row, 6, source, dataStyle);
            // 原始成绩
            createCell(row, 7, detail.getOriginal_score() != null ? detail.getOriginal_score().toString() : "-", dataStyle);
            // 绩点
            createCell(row, 8, calculateGPA(detail.getTotal_score()), dataStyle);
            // 等级
            createCell(row, 9, getScoreLevel(detail.getTotal_score()), dataStyle);
            // 状态
            createCell(row, 10, (detail.getTotal_score() != null && detail.getTotal_score() >= 60) ? "及格" : "不及格", dataStyle);
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // 设置最小宽度
            int width = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, Math.max(width, 3000));
        }
        
        return workbook;
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "-");
        cell.setCellStyle(style);
    }

    private String formatScore(Double score) {
        return score != null ? String.format("%.2f", score) : "-";
    }

    private String calculateGPA(Double score) {
        if (score == null) return "-";
        if (score >= 96) return "4.0";
        if (score >= 90) return "4.0";
        if (score >= 85) return "3.7";
        if (score >= 82) return "3.3";
        if (score >= 78) return "3.0";
        if (score >= 75) return "2.7";
        if (score >= 71) return "2.3";
        if (score >= 66) return "2.0";
        if (score >= 62) return "1.7";
        if (score >= 60) return "1.3";
        return "0";
    }

    private String getScoreLevel(Double score) {
        if (score == null) return "-";
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}
