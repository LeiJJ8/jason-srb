package com.happy.srb.core.controller.admin;


import com.happy.common.result.Result;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.pojo.entity.BorrowInfo;
import com.happy.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.happy.srb.core.service.BorrowInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/admin/core/borrowInfo")
public class AdminBorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    @GetMapping("/list")
    public Result getList(){
        List<BorrowInfo> borrowInfoList = borrowInfoService.getList();
        return Result.ok().data("list",borrowInfoList);
    }

    @GetMapping("/show/{id}")
    public Result show(@PathVariable Long id){
        Map<String, Object> borrowInfoDetail = borrowInfoService.getBorrowInfoDetail(id);
        return Result.ok().data("borrowInfoDetail",borrowInfoDetail);
    }

    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO){

        borrowInfoService.approval(borrowInfoApprovalVO);
        return Result.ok().message("审批完成");

    }
}

