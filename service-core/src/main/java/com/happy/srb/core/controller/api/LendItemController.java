package com.happy.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.happy.common.result.Result;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.hfb.RequestHelper;
import com.happy.srb.core.pojo.vo.InvestVO;
import com.happy.srb.core.service.LendItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Slf4j
@RestController
@RequestMapping("/api/core/lendItem")
public class LendItemController {

    @Resource
    private LendItemService lendItemService;

    @PostMapping("/auth/commitInvest")
    public Result commitInvest(@RequestBody InvestVO investVO, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String userName = JwtUtils.getUserName(token);
        investVO.setInvestUserId(userId);
        investVO.setInvestName(userName);

        String formStr = lendItemService.commitInvest(investVO);
        return Result.ok().data("formStr",formStr);
    }

    @PostMapping("/notify")
    public String toNotify(HttpServletRequest request){

        Map<String,Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户投资异步回调：" + JSON.toJSONString(paramMap));

        if(RequestHelper.isSignEquals(paramMap)){
            if("0001".equals(paramMap.get("resultCode"))){
                lendItemService.toNotify(paramMap);
            }else {
                log.info("用户投资异步回调失败：" + JSON.toJSONString(paramMap));
                return "fail";
            }
        }else{
            log.info("用户投资异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }
}

