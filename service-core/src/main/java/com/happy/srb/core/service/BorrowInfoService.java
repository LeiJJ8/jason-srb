package com.happy.srb.core.service;

import com.happy.srb.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.srb.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    BigDecimal getBorrowAmount(Long userId);

    void saveBorrowInfo(BorrowInfo borrowInfo,Long userId);

    Integer getStatusById(Long userId);

    List<BorrowInfo> getList();

    Map<String, Object> getBorrowInfoDetail(Long id);

    void approval(BorrowInfoApprovalVO borrowInfoApprovalVO);

}
