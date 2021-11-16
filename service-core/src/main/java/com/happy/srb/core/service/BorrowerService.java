package com.happy.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.srb.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.srb.core.pojo.vo.BorrowerApprovalVO;
import com.happy.srb.core.pojo.vo.BorrowerDetailVO;
import com.happy.srb.core.pojo.vo.BorrowerVO;

import java.math.BigDecimal;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface BorrowerService extends IService<Borrower> {

    void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId);

    Integer getBorrowerStatus(Long userId);

    IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword);

    BorrowerDetailVO show(Long id);

    void approval(BorrowerApprovalVO borrowerApprovalVO);

}
