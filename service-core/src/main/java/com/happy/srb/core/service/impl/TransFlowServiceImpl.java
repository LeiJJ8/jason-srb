package com.happy.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.srb.core.mapper.UserInfoMapper;
import com.happy.srb.core.pojo.bo.TransFlowBO;
import com.happy.srb.core.pojo.entity.TransFlow;
import com.happy.srb.core.mapper.TransFlowMapper;
import com.happy.srb.core.pojo.entity.UserInfo;
import com.happy.srb.core.service.TransFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.srb.core.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void saveTransFlow(TransFlowBO transFlowBO) {
        //获取用户基本信息 user_info
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code", transFlowBO.getBindCode());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);

        //存储交易流水数据
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(transFlowBO.getAgentBillNo());
        transFlow.setTransType(transFlowBO.getTransTypeEnum().getTransType());
        transFlow.setTransTypeName(transFlowBO.getTransTypeEnum().getTransTypeName());
        transFlow.setTransAmount(transFlowBO.getAmount());
        transFlow.setMemo(transFlowBO.getMemo());
        baseMapper.insert(transFlow);
    }

    @Override
    public boolean isSaveTransFlow(String agentBillNo) {
        QueryWrapper<TransFlow> queryWrapper = new QueryWrapper();
        queryWrapper.eq("trans_no", agentBillNo);
        int count = baseMapper.selectCount(queryWrapper);

        return count > 0;
    }
}
