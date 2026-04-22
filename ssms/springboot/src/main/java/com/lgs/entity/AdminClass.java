package com.lgs.entity;

import lombok.Data;

@Data
public class AdminClass {
    private Integer id;
    private String grade;
    private String major;
    private String class_name;
    private String class_code;
    private String counselor_id;
    private String counselor_name;

}