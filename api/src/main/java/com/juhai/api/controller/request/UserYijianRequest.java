package com.juhai.api.controller.request;

import com.juhai.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "意见请求类", description = "意见请求参数")
public class UserYijianRequest {

    /**
     * 手机号
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "意见内容", example = "意见内容", required = true)
    private String content;
}
