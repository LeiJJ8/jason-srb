package com.happy.srb.core.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.common.exception.Assert;
import com.happy.common.result.ResponseEnum;
import com.happy.common.result.Result;
import com.happy.common.util.RegexValidateUtils;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.pojo.vo.LoginVO;
import com.happy.srb.core.pojo.vo.RegisterVO;
import com.happy.srb.core.pojo.vo.UserInfoVO;
import com.happy.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.service.Tags;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Api(tags = "会员接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public Result register(@ApiParam(value = "注册对象",required = true)
                               @RequestBody RegisterVO registerVO){
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();

        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(code,ResponseEnum.CODE_NULL_ERROR);

        String dbCode = (String) redisTemplate.opsForValue().get("srb:sms:code:" + mobile);
        Assert.equals(code,dbCode,ResponseEnum.CODE_ERROR);

        userInfoService.register(registerVO);
        return Result.ok().message("注册成功");
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO, HttpServletRequest request){
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO,ip);

        return Result.ok().data("userInfo",userInfoVO);
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        System.out.println(token);
        boolean b = JwtUtils.checkToken(token);

        if(b){
            return Result.ok();
        }else {
            return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }
}

