package com.lgs.entity;

import lombok.Data;

@Data
public class ModifyRequest {
    private Integer id;
    private String user_id;
    private String user_name;
    private String user_type;
    private String field_name;
    private String old_value;
    private String new_value;
    private Integer status; // 0-待审核，1-已通过，2-已拒绝
    private String create_time;

}