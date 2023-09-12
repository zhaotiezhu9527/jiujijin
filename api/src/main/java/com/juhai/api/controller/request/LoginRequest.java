package com.juhai.api.controller.request;

import com.juhai.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "登录请求类", description = "登录请求参数")
public class LoginRequest {

    @NotNull(message = "system.phone.validerror")
    @Pattern(regexp = RegConstant.USER_PHONE_REG, message = "system.phone.validerror")
    @ApiModelProperty(value = "手机号", example = "46456456", required = true)
    private String phone;

    @NotNull(message = "validation.user.register.loginpwd")
    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.user.register.loginpwd")
    @ApiModelProperty(value = "登录密码", example = "123qwe", required = true)
    private String loginPwd;
}
