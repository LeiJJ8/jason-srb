package com.happy.srb.core.service;

import com.happy.srb.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface UserAccountService extends IService<UserAccount> {

    String commitCharge(BigDecimal chargeAmt, Long userId);

    String toNotify(Map<String, Object> paramMap);

    UserAccount getUserAccountByUserId(Long userId);

}
