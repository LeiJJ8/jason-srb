package com.happy.srb.core.controller.admin;


import com.happy.common.result.Result;
import com.happy.srb.core.pojo.entity.Lend;
import com.happy.srb.core.service.LendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.ResultSet;
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
@RequestMapping("/admin/core/lend")
public class AdminLendController {

    @Resource
    private LendService lendService;

    @GetMapping("/list")
    public Result list(){
        List<Lend> lendList = lendService.getList();
        return Result.ok().data("list",lendList);
    }

    @GetMapping("/show/{id}")
    public Result show(@PathVariable Long id){
        Map<String, Object> lendDetail = lendService.getLendDetail(id);
        return Result.ok().data("lendDetail",lendDetail);
    }

    @GetMapping("/makeLoan/{id}")
    public Result makeLoan(@PathVariable Long id){
        lendService.makeLoan(id);
        return Result.ok().message("放款成功");
    }
}

