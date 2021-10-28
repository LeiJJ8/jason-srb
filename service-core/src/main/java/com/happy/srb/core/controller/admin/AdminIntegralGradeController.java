package com.happy.srb.core.controller.admin;

import com.happy.common.result.Result;
import com.happy.srb.core.pojo.entity.IntegralGrade;
import com.happy.srb.core.service.IntegralGradeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LJJ
 * @create 2021-10-27 21:07
 */
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Autowired
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public Result listAll(){
        List<IntegralGrade> list = integralGradeService.list();
        return Result.ok().data("list",list);
    }

    @ApiOperation(value = "根据id删除积分等级",notes = "逻辑删除")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@ApiParam(value = "数据id",required = true,example = "1") @PathVariable("id") Long id){
        boolean flag = integralGradeService.removeById(id);
        if(flag){
            return Result.ok().message("删除成功");
        }else{
            return Result.error().message("删除失败");
        }
    }
}
