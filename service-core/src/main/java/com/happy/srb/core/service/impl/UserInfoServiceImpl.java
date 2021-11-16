package com.happy.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.common.exception.Assert;
import com.happy.common.exception.BusinessException;
import com.happy.common.result.ResponseEnum;
import com.happy.common.util.MD5;
import com.happy.srb.base.utils.JwtUtils;
import com.happy.srb.core.mapper.UserAccountMapper;
import com.happy.srb.core.mapper.UserLoginRecordMapper;
import com.happy.srb.core.pojo.entity.UserAccount;
import com.happy.srb.core.pojo.entity.UserInfo;
import com.happy.srb.core.mapper.UserInfoMapper;
import com.happy.srb.core.pojo.entity.UserLoginRecord;
import com.happy.srb.core.pojo.query.UserInfoQuery;
import com.happy.srb.core.pojo.vo.LoginVO;
import com.happy.srb.core.pojo.vo.RegisterVO;
import com.happy.srb.core.pojo.vo.UserInfoVO;
import com.happy.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Override
    public void register(RegisterVO registerVO) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",registerVO.getMobile());
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ResponseEnum.MOBILE_EXIST_ERROR);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVO.getUserType());
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
        userInfo.setHeadImg("https://ljj-srb-file.oss-cn-beijing.aliyuncs.com/avatar/21/11/08/9956e100-cd7a-479d-9333-53ad6361760e.jpg");
        baseMapper.insert(userInfo);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoVO login(LoginVO loginVO, String ip) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Integer userType = loginVO.getUserType();

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile)
                    .eq("user_type",userType);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);

        Assert.notNoll(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);

        Assert.equals(MD5.encrypt(password),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);

        Assert.equals(userInfo.getStatus(),UserInfo.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);

        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(userInfo.getId());
        userInfoVO.setToken(token);
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setMobile(userInfo.getMobile());
        userInfoVO.setUserType(userType);

        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery) {
        if(userInfoQuery == null){
            return baseMapper.selectPage(pageParam,null);
        }

        String mobile = userInfoQuery.getMobile();
        Integer userType = userInfoQuery.getUserType();
        Integer status = userInfoQuery.getStatus();

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(mobile),"mobile",mobile)
                    .eq(userType != null,"user_type",userType)
                    .eq(status != null,"status",status);

        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public String getMobileByBindCode(String bindCode) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code", bindCode);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        return userInfo.getMobile();
    }


}
