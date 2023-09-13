package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "意见请求类", description = "意见请求参数")
public class UserJiuJiJinRequest {

    /**
     * 手机号
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "任务id", example = "1", required = true)
    private String id;
}
