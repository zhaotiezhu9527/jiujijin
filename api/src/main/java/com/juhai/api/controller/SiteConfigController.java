package com.juhai.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.juhai.commons.entity.Version;
import com.juhai.commons.service.MessageTextService;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.service.VersionService;
import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "获取系统配置相关", tags = "获取系统配置相关")
@RequestMapping("/system")
@RestController
public class SiteConfigController {

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private MessageTextService messageTextService;

    @Autowired
    private VersionService versionService;

    @ApiOperation(value = "获取系统配置")
    @GetMapping("/config")
    public R config(HttpServletRequest httpServletRequest) {
        JSONObject obj = new JSONObject();
        Map<String, String> allParamByMap = paramterService.getAllParamByMap();
        obj.put("onlineService", allParamByMap.get("online_service"));
        String area = allParamByMap.get("area");
        obj.put("area", JSONArray.parse(area));

        String bank = allParamByMap.get("bank");
        obj.put("bank", JSONArray.parse(bank));
        return R.ok().put("data", obj);
    }

    @ApiOperation(value = "获取当前版本号")
    @GetMapping("/version")
    public R version(HttpServletRequest httpServletRequest) {
        List<Version> versions = versionService.list();
        Map<String, JSONObject> map = new HashMap<>();
        for (Version version : versions) {
            JSONObject object = new JSONObject();
            object.put("version", version.getVersion());
            object.put("url", version.getDownloadUrl());
            map.put(version.getPlatForm(), object);
        }
        return R.ok().put("data", map);
    }
}
