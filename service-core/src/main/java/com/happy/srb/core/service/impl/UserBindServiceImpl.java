package com.happy.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.common.exception.Assert;
import com.happy.common.result.ResponseEnum;
import com.happy.srb.core.enums.UserBindEnum;
import com.happy.srb.core.hfb.FormHelper;
import com.happy.srb.core.hfb.HfbConst;
import com.happy.srb.core.hfb.RequestHelper;
import com.happy.srb.core.mapper.UserInfoMapper;
import com.happy.srb.core.pojo.entity.UserBind;
import com.happy.srb.core.mapper.UserBindMapper;
import com.happy.srb.core.pojo.entity.UserInfo;
import com.happy.srb.core.pojo.vo.UserBindVO;
import com.happy.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public String bind(UserBindVO userBindVO, Long userId) {
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_card",userBindVO.getIdCard())
                .ne("user_id",userId);
        UserBind userBind = baseMapper.selectOne(queryWrapper);
        Assert.isNull(userBind, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        userBind = baseMapper.selectOne(queryWrapper);

        if(userBind == null){
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindVO,userBind);
            userBind.setUserId(userId);
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
            baseMapper.insert(userBind);
        }else{
            BeanUtils.copyProperties(userBindVO,userBind);
            baseMapper.updateById(userBind);
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard",userBindVO.getIdCard());
        paramMap.put("personalName", userBindVO.getName());
        paramMap.put("bankType", userBindVO.getBankType());
        paramMap.put("bankNo", userBindVO.getBankNo());
        paramMap.put("mobile", userBindVO.getMobile());
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        String formStr = FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);

        return formStr;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void toNotify(Map<String, Object> stringObjectMap) {

        String bindCode = (String) stringObjectMap.get("bindCode");

        String agentUserId = (String) stringObjectMap.get("agentUserId");

        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",agentUserId);
        // 更新用户绑定表
        UserBind userBind = baseMapper.selectOne(queryWrapper);
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        baseMapper.updateById(userBind);
        // 更新用户表
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setBindCode(bindCode);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public String getBindCodeByUserId(Long userId) {
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserBind userBind = baseMapper.selectOne(queryWrapper);
        return userBind.getBindCode();
    }
}
