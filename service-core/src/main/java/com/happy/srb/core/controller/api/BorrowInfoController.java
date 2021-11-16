package com.happy.srb.core.controller.api;


import com.happy.common.result.Result;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.pojo.entity.BorrowInfo;
import com.happy.srb.core.service.BorrowInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/api/core/borrowInfo")
public class BorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    @GetMapping("/auth/getBorrowAmount")
    public Result getBorrowAmount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        BigDecimal borrowAmount = borrowInfoService.getBorrowAmount(userId);
        return Result.ok().data("borrowAmount",borrowAmount);
    }

    @PostMapping("/auth/save")
    public Result save(@RequestBody BorrowInfo borrowInfo,HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        borrowInfoService.saveBorrowInfo(borrowInfo,userId);
        return Result.ok().message("提交成功");
    }

    @GetMapping("/auth/getBorrowerStatus")
    public Result getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Integer borrowInfoStatus = borrowInfoService.getStatusById(userId);
        return Result.ok().data("borrowInfoStatus",borrowInfoStatus);
    }
}

