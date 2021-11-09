package com.happy.srb.core.controller.admin;


import com.happy.common.result.Result;
import com.happy.srb.core.pojo.entity.UserLoginRecord;
import com.happy.srb.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Api(tags = "会员登录日志接口")
@Slf4j
@RestController
@RequestMapping("/admin/core/userLoginRecord")
public class UserLoginRecordController {

    @Resource
    private UserLoginRecordService userLoginRecordService;

    @ApiOperation("显示用户登录日志")
    @GetMapping("/listTop50/{userId}")
    public Result getuserLoginRecordTop50(@ApiParam(value = "用户id",required = true)
                                          @PathVariable Long userId){

        List<UserLoginRecord> userLoginRecordList = userLoginRecordService.listTop50(userId);
        return Result.ok().data("list",userLoginRecordList);
    }
}

