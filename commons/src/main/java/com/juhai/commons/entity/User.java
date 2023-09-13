package com.juhai.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 登录密码
     */
    @TableField(value = "login_pwd")
    private String loginPwd;

    /**
     * 1:男 2:女
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 国籍
     */
    @TableField(value = "nationality")
    private String nationality;

    /**
     * 生日
     */
    @TableField(value = "birth")
    private Date birth;

    /**
     * 身份证号码或护照号
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 工作
     */
    @TableField(value = "work")
    private String work;

    /**
     * 省/市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 地区
     */
    @TableField(value = "region")
    private String region;

    /**
     * 银行名称
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 银行卡号
     */
    @TableField(value = "bank_card")
    private String bankCard;

    /**
     * 用户状态(1:正常 0:冻结)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 注册时间
     */
    @TableField(value = "register_time")
    private Date registerTime;

    /**
     * 注册Ip
     */
    @TableField(value = "register_ip")
    private String registerIp;

    /**
     * 上次登录时间
     */
    @TableField(value = "last_time")
    private Date lastTime;

    /**
     * 上次登录IP
     */
    @TableField(value = "last_ip")
    private String lastIp;

    /**
     * 最后修改时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    /**
     * 详细地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}