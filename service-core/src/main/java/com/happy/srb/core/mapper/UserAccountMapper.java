package com.happy.srb.core.mapper;

import com.happy.srb.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    void updateAccount( @Param("bindCode")String bindCode,
                        @Param("amount")BigDecimal amount,
                        @Param("freezeAmount")BigDecimal freezeAmount);
}
