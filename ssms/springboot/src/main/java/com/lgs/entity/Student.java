package com.lgs.entity;

import lombok.Data;

/**
 * 学生
*/
@Data
public class Student extends Account {

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
    /** 年龄 */
    private String age;
    /** 性别 */
    private String gender;
    /** 电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 专业 */
    private String profession;
    /** 角色标识 */
    private String role;
    /** 审核状态：0-待审核，1-已通过，2-已拒绝 */
    private Integer status;
    /** 密码版本号（用于检测密码是否被重置） */
    private Long passwordVersion;

}