package com.happy.srb.core.service.impl;

import com.happy.srb.core.pojo.entity.IntegralGrade;
import com.happy.srb.core.mapper.IntegralGradeMapper;
import com.happy.srb.core.service.IntegralGradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 积分等级表 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class IntegralGradeServiceImpl extends ServiceImpl<IntegralGradeMapper, IntegralGrade> implements IntegralGradeService {

    @Override
    public boolean updateGrade(IntegralGrade integralGrade) {
        return baseMapper.updateById(integralGrade) > 0;
    }
}
