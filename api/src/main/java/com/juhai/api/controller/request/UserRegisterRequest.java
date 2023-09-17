package com.juhai.api.controller.request;

import com.juhai.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@ApiModel(value = "注册请求类", description = "注册请求参数")
public class UserRegisterRequest {

    /**
     * 手机号
     */
    @NotNull(message = "system.phone.validerror")
    @Pattern(regexp = RegConstant.USER_PHONE_REG, message = "system.phone.validerror")
    @ApiModelProperty(value = "手机号", example = "46456456", required = true)
    private String phone;

    @NotNull(message = "validation.user.register.loginpwd")
    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.user.register.loginpwd")
    @ApiModelProperty(value = "登录密码", example = "123qwe", required = true)
    private String loginPwd;

    /**
     * 姓名
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "姓名", example = "张三", required = true)
    private String realName;

    /**
     * 1:男 2:女
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "性别", example = "1:男 2:女", required = true)
    private Integer gender;

    /**
     * 国籍
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "国籍", example = "国籍", required = true)
    private String nationality;

    /**
     * 生日
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "生日", example = "2003-03-08", required = true)
    private String birth;

    /**
     * 身份证号码或护照号
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "身份证号码或护照号", example = "3203222228852885", required = true)
    private String idCard;

    /**
     * 工作
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "工作", example = "工作", required = true)
    private String work;

    /**
     * 省/市
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "省/市", example = "省/市", required = true)
    private String city;

    /**
     * 地区
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "地区", example = "地区", required = true)
    private String region;

    /**
     * 银行名称
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行名称", example = "银行名称", required = true)
    private String bankName;

    /**
     * 银行卡号
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行卡号", example = "银行卡号", required = true)
    private String bankCard;

    /**
     * 详细地址
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "详细地址", example = "详细地址", required = true)
    private String address;

    /**
     * 邮箱
     */
//    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "邮箱", example = "邮箱", required = true)
    private String email;

    /**
     * 邮箱
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "验证码", example = "验证码", required = true)
    private String yzm;
}
