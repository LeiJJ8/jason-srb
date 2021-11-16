package com.happy.srb.core.controller.api;


import com.happy.common.result.Result;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.pojo.vo.BorrowerVO;
import com.happy.srb.core.service.BorrowerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/api/core/borrower")
public class BorrowerController {

    @Resource
    private BorrowerService borrowerService;

    @PostMapping("/auth/save")
    public Result save(@RequestBody BorrowerVO borrowerVO, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        borrowerService.saveBorrowerVOByUserId(borrowerVO,userId);
        return Result.ok().message("提交成功");
    }

    @GetMapping("/auth/getBorrowerStatus")
    public Result getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Integer borrowerStatus = borrowerService.getBorrowerStatus(userId);
        return Result.ok().data("borrowerStatus",borrowerStatus);
    }


}

