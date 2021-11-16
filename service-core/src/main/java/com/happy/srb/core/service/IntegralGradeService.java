package com.happy.srb.core.service;

import com.happy.srb.core.pojo.entity.IntegralGrade;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 积分等级表 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface IntegralGradeService extends IService<IntegralGrade> {

    boolean updateGrade(IntegralGrade integralGrade);
}
