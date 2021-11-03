package com.happy.srb.sms.controller.api;

import com.google.gson.Gson;
import com.happy.common.exception.Assert;
import com.happy.common.result.ResponseEnum;
import com.happy.common.result.Result;
import com.happy.common.util.RandomUtils;
import com.happy.common.util.RegexValidateUtils;
import com.happy.srb.sms.service.SmsService;
import com.happy.srb.sms.utils.SmsProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author LeiJJ
 * @date 2021-11-03 19:55
 */
@RestController
@RequestMapping("/api/sms")
@CrossOrigin
@Slf4j
@Api(tags = "短信管理")
public class ApiSmsController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SmsService service;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public Result send(@ApiParam(value = "手机号",required = true)
                       @PathVariable("mobile") String mobile){

        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);

        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        String code = RandomUtils.getSixBitRandom();
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("code",code);

        service.send(mobile, SmsProperties.TEMPLATE_CODE, codeMap);

        redisTemplate.opsForValue().set("code",code,5, TimeUnit.MINUTES);

        return Result.ok().message("短信发送成功");
    }
}
