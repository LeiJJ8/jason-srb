package com.happy.srb.core.service;

import com.happy.srb.core.pojo.bo.TransFlowBO;
import com.happy.srb.core.pojo.entity.TransFlow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface TransFlowService extends IService<TransFlow> {
    void saveTransFlow(TransFlowBO transFlowBO);

    boolean isSaveTransFlow(String agentBillNo);
}
