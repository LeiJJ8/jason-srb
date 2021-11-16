package com.happy.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.happy.common.result.Result;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.hfb.RequestHelper;
import com.happy.srb.core.pojo.entity.UserAccount;
import com.happy.srb.core.service.UserAccountService;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Slf4j
@RestController
@RequestMapping("/api/core/userAccount")
public class UserAccountController {

    @Resource
    private UserAccountService userAccountService;

    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public Result commitCharge(@PathVariable BigDecimal chargeAmt, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);

        String formStr = userAccountService.commitCharge(chargeAmt,userId);
        return Result.ok().data("formStr",formStr);
    }

    @PostMapping("/notify")
    public String toNotify(HttpServletRequest request){
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户充值异步回调：" + JSON.toJSONString(paramMap));

        //校验签名
        if(RequestHelper.isSignEquals(paramMap)) {
            //充值成功交易
            if("0001".equals(paramMap.get("resultCode"))) {
                return userAccountService.toNotify(paramMap);
            } else {
                log.info("用户充值异步回调充值失败：" + JSON.toJSONString(paramMap));
                return "success";
            }
        } else {
            log.info("用户充值异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
    }

    @GetMapping("/auth/getUserAccount")
    public Result getUserAccountByUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        UserAccount userAccount = userAccountService.getUserAccountByUserId(userId);
        return Result.ok().data("userAccount",userAccount);
    }

    @GetMapping("/auth/getAccount")
    public Result getAccount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        BigDecimal account = userAccountService.getUserAccountByUserId(userId).getAmount();
        return Result.ok().data("account",account);
    }
}

