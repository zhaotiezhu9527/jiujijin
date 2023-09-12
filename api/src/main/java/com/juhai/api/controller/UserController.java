package com.juhai.api.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.juhai.api.controller.request.LoginRequest;
import com.juhai.api.controller.request.UpdatePwdRequest;
import com.juhai.api.controller.request.UserRegisterRequest;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.Opinion;
import com.juhai.commons.entity.User;
import com.juhai.commons.entity.UserLog;
import com.juhai.commons.service.*;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import com.juhai.commons.utils.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "用户相关", tags = "用户")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private ParamterService paramterService;

    @Value("${token.expire}")
    private int expire;

    @Autowired
    private AvatarService avatarService;

    @Autowired
    private OpinionService opinionService;

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R registerV2(@Validated UserRegisterRequest request, HttpServletRequest httpServletRequest) throws Exception {
        String userName = request.getPhone().toLowerCase();
        String clientIP = ServletUtil.getClientIPByHeader(httpServletRequest, "x-original-forwarded-for");
        // 查询用户名是否存在
        long exist = userService.count(new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
        if (exist > 0) {
            return R.error(MsgUtil.get("system.user.register.exist"));
        }

        Date now = new Date();
        User user = new User();
        user.setUserName(request.getPhone());
        user.setRealName(request.getRealName());
        user.setLoginPwd(SecureUtil.md5(request.getLoginPwd()));
        user.setGender(request.getGender());
        user.setNationality(request.getNationality());
        user.setBirth(request.getBirth());
        user.setIdCard(request.getIdCard());
        user.setWork(request.getWork());
        user.setCity(request.getCity());
        user.setRegion(request.getRegion());
        user.setBankName(request.getBankName());
        user.setBankCard(request.getBankCard());
        user.setStatus(1);
        user.setRegisterTime(now);
        user.setRegisterIp(clientIP);
        user.setLastTime(now);
        user.setLastIp(clientIP);
        user.setModifyTime(now);
        userService.save(user);

        // 登录日志
        UserLog log = new UserLog();
        log.setUserName(userName);
        log.setIp(clientIP);
        log.setIpDetail(null);
        log.setLoginTime(new Date());
        userLogService.save(log);

        /** 保存token **/
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("userIp", clientIP);
        map.put("random", RandomUtil.randomString(6));
        String token = JwtUtils.getToken(map);
        redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(userName), token, expire, TimeUnit.MINUTES);
        return R.ok().put("token", token);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        redisTemplate.delete(RedisKeyUtil.UserTokenKey(userName));
        return R.ok();
    }

    @ApiOperation(value = "申请救济金")
    @PostMapping("/opinion/apply")
    public R opinionApply(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        Opinion opinion = opinionService.getOne(new LambdaQueryWrapper<Opinion>().eq(Opinion::getUserName, userName));
        if (opinion != null) {
            if (opinion.getStatus().intValue() == 0) {
                // 待审核
                return R.error(MsgUtil.get("system.opinion.apply.0"));
            } else if (opinion.getStatus().intValue() == 1) {
                // 申请失败
                return R.error(MsgUtil.get("system.opinion.apply.1"));
            } else {
                // 申请成功
                return R.error(MsgUtil.get("system.opinion.apply.2"));
            }
        }
        Opinion sava = new Opinion();
        sava.setUserName(userName);
        sava.setStatus(0);
        sava.setCreateTime(new Date());
        sava.setModifyTime(new Date());
        opinionService.save(sava);
        return R.ok(MsgUtil.get("system.opinion.apply.3"));
    }

    @ApiOperation(value = "救济金进度")
    @PostMapping("/opinion/schedule")
    public R opinionSchedule(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        Opinion opinion = opinionService.getOne(new LambdaQueryWrapper<Opinion>().eq(Opinion::getUserName, userName));
        return R.ok().put("status", opinion.getStatus());
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(@Validated LoginRequest request, HttpServletRequest httpServletRequest) {
        String clientIP = ServletUtil.getClientIPByHeader(httpServletRequest, "x-original-forwarded-for");
        // 查询用户信息
        User user = userService.getUserByName(request.getPhone());
        if (user == null) {
            return R.error(MsgUtil.get("system.phone.not.reg"));
        }

        if (user.getStatus().intValue() == 0) {
            return R.error(MsgUtil.get("system.user.enable"));
        }

        // 获取所有参数配置
        Map<String, String> paramsMap = paramterService.getAllParamByMap();

        String incKey = RedisKeyUtil.LoginPwdErrorKey(request.getPhone());
        /** 每日错误次数上限 **/
        String dayCount = redisTemplate.opsForValue().get(incKey);
        int count = NumberUtils.toInt(dayCount, 0);
        Integer pwdErrCount = MapUtil.getInt(paramsMap, "login_pwd_error", 0);
        if (pwdErrCount > 0 && count >= pwdErrCount) {
            return R.error(MsgUtil.get("system.user.login.pwd.limit"));
        }

        // 验证密码正确
        String pwd = SecureUtil.md5(request.getLoginPwd());
        if (!StringUtils.equals(pwd, user.getLoginPwd())) {
            /** 累计密码错误 **/
            redisTemplate.opsForValue().increment(incKey);
            redisTemplate.expire(incKey, 1, TimeUnit.DAYS);
            return R.error(MsgUtil.get("system.user.login.pwd.error"));
        }

        Date now = new Date();
        /** 更新最后登录时间 **/
        userService.update(
                new UpdateWrapper<User>().lambda()
                        .eq(User::getId, user.getId())
                        .set(User::getLastIp, clientIP)
                        .set(User::getLastTime, now)
        );

        // 登录日志
        UserLog log = new UserLog();
        log.setUserName(request.getPhone());
        log.setIp(clientIP);
        log.setIpDetail(null);
        log.setLoginTime(new Date());
        userLogService.save(log);

        /** 保存token **/
        Map<String, String> map = new HashMap<>();
        map.put("userName", request.getPhone());
        map.put("userIp", clientIP);
        map.put("random", RandomUtil.randomString(6));
        String token = JwtUtils.getToken(map);
        redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(user.getUserName()), token, expire, TimeUnit.MINUTES);

        /** 删除密码输入错误次数 **/
        redisTemplate.delete(incKey);
        return R.ok().put("token", token);
    }

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        User user = userService.getUserByName(userName);
        JSONObject temp = new JSONObject();
        temp.put("phone", user.getUserName());
        temp.put("realName", user.getRealName());
        temp.put("gender", user.getGender());
        temp.put("nationality", user.getNationality());
        temp.put("birth", user.getBirth());
        temp.put("idCard", user.getIdCard());
        temp.put("work", user.getWork());
        temp.put("city", user.getCity());
        temp.put("region", user.getRegion());
        temp.put("bank_name", user.getBankName());
        temp.put("bank_card", user.getBankCard());
        return R.ok().put("data", temp);
    }
}
