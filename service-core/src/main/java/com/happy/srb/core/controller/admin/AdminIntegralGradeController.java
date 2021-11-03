package com.happy.srb.core.controller.admin;

import com.happy.common.exception.Assert;
import com.happy.common.result.ResponseEnum;
import com.happy.common.result.Result;
import com.happy.srb.core.pojo.entity.IntegralGrade;
import com.happy.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author LeiJJ
 * @date 2021-10-27 21:07
 */
@Api(tags = "积分等级管理")
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public Result listAll(){
        List<IntegralGrade> list = integralGradeService.list();
        return Result.ok().data("list",list);
    }

    @ApiOperation(value = "根据id删除积分等级",notes = "逻辑删除")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@ApiParam(value = "数据id",required = true,example = "1")
                                 @PathVariable("id") Long id){
        boolean flag = integralGradeService.removeById(id);
        if(flag){
            return Result.ok().message("删除成功");
        }else{
            return Result.error().message("删除失败");
        }
    }

    @ApiOperation(value = "新增积分等级")
    @PostMapping("/save")
    public Result save(@ApiParam(value = "积分等级对象",required = true)
                           @RequestBody IntegralGrade integralGrade){

        //if(integralGrade.getBorrowAmount() == null)
        //    throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        Assert.nutNoll(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        boolean save = integralGradeService.save(integralGrade);
        if(save){
            return Result.ok().message("保存成功");
        }else {
            return Result.error().message("保存失败");
        }
    }

    @ApiOperation("根据id查询积分等级")
    @GetMapping("/get/{id}")
    public Result getById(@ApiParam(value = "查询数据id",required = true,example = "1")
                              @PathVariable("id") Integer id){
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if(integralGrade != null){
            return Result.ok().data("integralGrade",integralGrade);
        }else{
            return Result.error().message("数据不存在");
        }
    }

    @ApiOperation("更新积分等级")
    @PutMapping("/update")
    public Result updateById(@ApiParam(value = "积分等级对象",required = true)
                               @RequestBody IntegralGrade integralGrade){
        boolean update = integralGradeService.updateById(integralGrade);
        if(update){
            return Result.ok().message("更新成功");
        }else{
            return Result.error().message("更新失败");
        }
    }
}
