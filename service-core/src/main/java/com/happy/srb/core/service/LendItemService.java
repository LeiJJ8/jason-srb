package com.happy.srb.core.service;

import com.happy.srb.core.pojo.entity.LendItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.srb.core.pojo.vo.InvestVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface LendItemService extends IService<LendItem> {

    String commitInvest(InvestVO investVO);

    void toNotify(Map<String, Object> paramMap);

    List<LendItem> selectByLendId(Long lendId, int status);
}
