package com.happy.srb.sms.controller.api;

import com.google.gson.Gson;
import com.happy.common.exception.Assert;
import com.happy.common.result.ResponseEnum;
import com.happy.common.result.Result;
import com.happy.common.util.RandomUtils;
import com.happy.common.util.RegexValidateUtils;
import com.happy.srb.sms.client.CoreUserInfoClient;
import com.happy.srb.sms.service.SmsService;
import com.happy.srb.sms.utils.SmsProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LeiJJ
 * @date 2021-11-03 19:55
 */
@RestController
@RequestMapping("/api/sms")
@Slf4j
@Api(tags = "短信管理")
public class ApiSmsController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SmsService service;
    @Resource
    private CoreUserInfoClient client;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public Result send(@ApiParam(value = "手机号",required = true)
                       @PathVariable("mobile") String mobile){

        // 手机号是否已注册
        boolean isRegister = client.checkMobile(mobile);
        Assert.isTrue(!isRegister,ResponseEnum.MOBILE_EXIST_ERROR);
        // 手机号是否为空
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        // 手机号是否正确
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        String code = RandomUtils.getSixBitRandom();
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("code",code);

        service.send(mobile, SmsProperties.TEMPLATE_CODE, codeMap);

        //redisTemplate.opsForValue().set("code",code,5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile,code);

        return Result.ok().message("短信发送成功");
    }
}
