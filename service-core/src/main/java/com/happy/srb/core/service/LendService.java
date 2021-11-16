package com.happy.srb.core.service;

import com.happy.srb.core.pojo.entity.BorrowInfo;
import com.happy.srb.core.pojo.entity.Lend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.srb.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface LendService extends IService<Lend> {

    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo);

    List<Lend> getList();

    Map<String, Object> getLendDetail(Long id);

    BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalMonth, Integer returnMethod);
}
