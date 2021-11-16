package com.happy.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.happy.common.result.Result;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.hfb.RequestHelper;
import com.happy.srb.core.pojo.vo.UserBindVO;
import com.happy.srb.core.service.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Api(tags = "会员账号绑定")
@Slf4j
@RestController
@RequestMapping("/api/core/userBind")
public class UserBindController {

    @Resource
    private UserBindService userBindService;

    @ApiOperation("账号绑定提交数据")
    @PostMapping("/auth/bind")
    public Result bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr = userBindService.bind(userBindVO,userId);
        return Result.ok().data("formStr",formStr);
    }

    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String toNotify(HttpServletRequest request){
        Map<String, Object> stringObjectMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户账号绑定异步回调：" + JSON.toJSONString(stringObjectMap));

        if(!RequestHelper.isSignEquals(stringObjectMap)){
            log.error("用户账号绑定异步回调签名错误：" + JSON.toJSONString(stringObjectMap));
            return  "fail";
        }

        userBindService.toNotify(stringObjectMap);
        return "success";
    }

    @GetMapping("/getBindCodeByUserId/{userId}")
    public Result getBindCodeByUserId(@PathVariable Long userId){
        String bindCode = userBindService.getBindCodeByUserId(userId);
        return Result.ok().data("bindCode",bindCode);
    }
}

