package com.lgs.entity;

import lombok.Data;

/**
 * 管理员
*/
@Data
public class Teacher extends Account {

    /** ID */
    private Integer id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 头像 */
    private String avatar;
    /** 性别 */
    private String gender;
    /** 电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 角色标识 */
    private String role;
    /** 审核状态：0-待审核，1-已通过，2-已拒绝 */
    private Integer status;

}