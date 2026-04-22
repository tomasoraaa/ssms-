package com.lgs.entity;

import lombok.Data;

@Data
public class StudentAdminClass {
    private Integer id;
    private String student_id;
    private Integer admin_class_id;
    private String enroll_year;
    private Integer status;
    private String student_name;
    private String class_name;
    private String class_code;

}