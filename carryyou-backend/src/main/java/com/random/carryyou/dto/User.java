package com.random.carryyou.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *
 * @TableName tb_user
 */
@Data
public class User implements Serializable {
    /**
     * 主键id
     */
    private Object id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 用户状态(01:正常,99:注销)
     */
    private String status;

    private String statusDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    private String updatedBy;

    private String token;

    private String roleId;

    private String roleName;

    private static final long serialVersionUID = 1L;

}
