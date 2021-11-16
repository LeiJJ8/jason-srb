package com.happy.srb.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.common.exception.Assert;
import com.happy.common.result.ResponseEnum;
import com.happy.srb.base.dto.SmsDTO;
import com.happy.srb.core.enums.TransTypeEnum;
import com.happy.srb.core.hfb.FormHelper;
import com.happy.srb.core.hfb.HfbConst;
import com.happy.srb.core.hfb.RequestHelper;
import com.happy.srb.core.mapper.UserInfoMapper;
import com.happy.srb.core.mapper.UserLoginRecordMapper;
import com.happy.srb.core.pojo.bo.TransFlowBO;
import com.happy.srb.core.pojo.entity.UserAccount;
import com.happy.srb.core.mapper.UserAccountMapper;
import com.happy.srb.core.pojo.entity.UserInfo;
import com.happy.srb.core.pojo.entity.UserLoginRecord;
import com.happy.srb.core.service.TransFlowService;
import com.happy.srb.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.srb.core.service.UserInfoService;
import com.happy.srb.core.util.LendNoUtils;
import com.happy.srb.rabbitutil.constant.MQConst;
import com.happy.srb.rabbitutil.service.MQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Slf4j
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private TransFlowService transFlowService;

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private MQService mqService;

    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();

        Assert.notEmpty(bindCode, ResponseEnum.USER_NO_BIND_ERROR);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getNo());
        paramMap.put("bindCode", bindCode);
        paramMap.put("chargeAmt", chargeAmt);
        paramMap.put("feeAmt", new BigDecimal("0"));
        paramMap.put("notifyUrl", HfbConst.RECHARGE_NOTIFY_URL);//检查常量是否正确
        paramMap.put("returnUrl", HfbConst.RECHARGE_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        String formStr = FormHelper.buildForm(HfbConst.RECHARGE_URL, paramMap);
        return formStr;
    }

    @Override
    public String toNotify(Map<String, Object> paramMap) {
        log.info("充值成功：" + JSONObject.toJSONString(paramMap));

        //判断交易流水是否存在
        String agentBillNo = (String)paramMap.get("agentBillNo"); //商户充值订单号
        boolean isSave = transFlowService.isSaveTransFlow(agentBillNo);
        if(isSave){
            log.warn("幂等性返回");
            return "fail";
        }

        String bindCode = (String)paramMap.get("bindCode"); //充值人绑定协议号
        String chargeAmt = (String)paramMap.get("chargeAmt"); //充值金额

        //优化
        baseMapper.updateAccount(bindCode, new BigDecimal(chargeAmt), new BigDecimal(0));

        //增加交易流水
        //agentBillNo = (String)paramMap.get("agentBillNo"); //商户充值订单号
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(chargeAmt),
                TransTypeEnum.RECHARGE,
                "充值");
        transFlowService.saveTransFlow(transFlowBO);

        log.info("发消息");
        String mobile = userInfoService.getMobileByBindCode(bindCode);
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setMobile(mobile);
        smsDTO.setMessage("666666");
        mqService.sendMessage(MQConst.EXCHANGE_TOPIC_SMS,MQConst.ROUTING_SMS_ITEM,smsDTO);

        return "success";
    }

    @Override
    public UserAccount getUserAccountByUserId(Long userId) {
        QueryWrapper<UserAccount> userAccountQueryWrapper = new QueryWrapper<>();
        userAccountQueryWrapper.eq("user_id",userId);
        UserAccount userAccount = baseMapper.selectOne(userAccountQueryWrapper);

        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper.eq("user_id",userId)
                                    .orderByDesc("id");
        List<UserLoginRecord> userLoginRecordList = userLoginRecordMapper.selectList(userLoginRecordQueryWrapper);
        UserLoginRecord userLoginRecord = userLoginRecordList.get(1);
        userAccount.setLoginRecord(userLoginRecord.getCreateTime());

        return userAccount;
    }

}
