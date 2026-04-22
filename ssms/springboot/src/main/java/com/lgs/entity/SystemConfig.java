package com.lgs.entity;

import lombok.Data;

@Data
public class SystemConfig {
    private Integer id;
    private String config_key;
    private String config_value;
    private String description;

}