package com.happy.srb.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.common.result.Result;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.pojo.entity.Borrower;
import com.happy.srb.core.pojo.vo.BorrowerApprovalVO;
import com.happy.srb.core.pojo.vo.BorrowerDetailVO;
import com.happy.srb.core.pojo.vo.BorrowerVO;
import com.happy.srb.core.service.BorrowerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/admin/core/borrower")
public class AdminBorrowerController {

    @Resource
    private BorrowerService borrowerService;


    @GetMapping("/list/{page}/{limit}")
    public Result list(@PathVariable Integer page,
                       @PathVariable Integer limit,
                       String keyword){

        Page<Borrower> pageParam = new Page<>(page,limit);
        IPage<Borrower> pageModel = borrowerService.listPage(pageParam,keyword);
        return Result.ok().data("pageModel",pageModel);
    }

    @GetMapping("/showInfo/{id}")
    public Result showInfo(@PathVariable Long id){
        BorrowerDetailVO borrowerDetailVO = borrowerService.show(id);
        return Result.ok().data("borrowerDetailVO",borrowerDetailVO);
    }

    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO){
        borrowerService.approval(borrowerApprovalVO);
        return Result.ok().message("审批成功");
    }
}

