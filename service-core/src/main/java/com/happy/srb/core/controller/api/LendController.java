package com.happy.srb.core.controller.api;


import com.baomidou.mybatisplus.extension.api.R;
import com.happy.common.result.Result;
import com.happy.srb.core.pojo.entity.Lend;
import com.happy.srb.core.service.LendService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/api/core/lend")
public class LendController {

    @Resource
    private LendService lendService;

    @GetMapping("/list")
    public Result list(){
        List<Lend> lendList = lendService.getList();
        return Result.ok().data("lendList",lendList);
    }

    @GetMapping("/show/{id}")
    public Result show(@PathVariable Long id){
        Map<String, Object> lendDetail = lendService.getLendDetail(id);
        return Result.ok().data("lendDetail",lendDetail);
    }

    @GetMapping("/getInterestCount/{invest}/{yearRate}/{totalMonth}/{returnMethod}")
    public Result getInterestCount(@ApiParam(value = "投资金额", required = true)
                                       @PathVariable("invest") BigDecimal invest,

                                   @ApiParam(value = "年化收益", required = true)
                                       @PathVariable("yearRate")BigDecimal yearRate,

                                   @ApiParam(value = "期数", required = true)
                                       @PathVariable("totalMonth")Integer totalMonth,

                                   @ApiParam(value = "还款方式", required = true)
                                       @PathVariable("returnMethod")Integer returnMethod){
        BigDecimal  interestCount = lendService.getInterestCount(invest, yearRate, totalMonth, returnMethod);
        return Result.ok().data("interestCount", interestCount);
    }
}

