package com.happy.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.common.exception.Assert;
import com.happy.common.result.ResponseEnum;
import com.happy.srb.core.enums.BorrowInfoStatusEnum;
import com.happy.srb.core.enums.BorrowerStatusEnum;
import com.happy.srb.core.enums.UserBindEnum;
import com.happy.srb.core.mapper.BorrowerMapper;
import com.happy.srb.core.mapper.IntegralGradeMapper;
import com.happy.srb.core.mapper.UserInfoMapper;
import com.happy.srb.core.pojo.entity.BorrowInfo;
import com.happy.srb.core.mapper.BorrowInfoMapper;
import com.happy.srb.core.pojo.entity.Borrower;
import com.happy.srb.core.pojo.entity.IntegralGrade;
import com.happy.srb.core.pojo.entity.UserInfo;
import com.happy.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.happy.srb.core.pojo.vo.BorrowerDetailVO;
import com.happy.srb.core.service.BorrowInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.srb.core.service.BorrowerService;
import com.happy.srb.core.service.DictService;
import com.happy.srb.core.service.LendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private IntegralGradeMapper integralGradeMapper;

    @Resource
    public DictService dictService;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private BorrowerService borrowerService;

    @Resource
    private LendService lendService;

    @Override
    public BigDecimal getBorrowAmount(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        Integer integral = userInfo.getIntegral();

        QueryWrapper<IntegralGrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("integral_start",integral)
                    .ge("integral_end",integral);

        IntegralGrade integralGrade = integralGradeMapper.selectOne(queryWrapper);
        if(integralGrade == null){
            return new BigDecimal(0);
        }
        return integralGrade.getBorrowAmount();
    }

    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo, Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //判断用户绑定状态
        Assert.isTrue(
                userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_BIND_ERROR);

        //判断用户信息是否审批通过
        Assert.isTrue(
                userInfo.getBorrowAuthStatus().intValue() == BorrowerStatusEnum.AUTH_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_AMOUNT_ERROR);

        //判断借款额度是否足够
        BigDecimal borrowAmount = this.getBorrowAmount(userId);
        Assert.isTrue(
                borrowInfo.getAmount().doubleValue() <= borrowAmount.doubleValue(),
                ResponseEnum.USER_AMOUNT_LESS_ERROR);

        borrowInfo.setUserId(userId);
        borrowInfo.setBorrowYearRate(borrowInfo.getBorrowYearRate().divide(new BigDecimal(100)));
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());

        baseMapper.insert(borrowInfo);
    }

    @Override
    public Integer getStatusById(Long userId) {
        QueryWrapper<BorrowInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status").eq("user_id",userId);
        List<Object> objects = baseMapper.selectObjs(queryWrapper);

        if(objects.size() <= 0){
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }

        return (Integer) objects.get(0);
    }

    @Override
    public List<BorrowInfo> getList() {
        List<BorrowInfo> borrowInfoList = baseMapper.selectBorrowInfoList();
        borrowInfoList.forEach(borrowInfo -> {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
            String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
            String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
            borrowInfo.getParam().put("returnMethod",returnMethod);
            borrowInfo.getParam().put("moneyUse",moneyUse);
            borrowInfo.getParam().put("status",status);
        });

        return borrowInfoList;
    }

    @Override
    public Map<String, Object> getBorrowInfoDetail(Long id) {

        BorrowInfo borrowInfo = baseMapper.selectById(id);

        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
        String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
        borrowInfo.getParam().put("returnMethod",returnMethod);
        borrowInfo.getParam().put("moneyUse",moneyUse);
        borrowInfo.getParam().put("status",status);

        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",borrowInfo.getUserId());
        Borrower borrower = borrowerMapper.selectOne(queryWrapper);

        BorrowerDetailVO borrowerDetailVO = borrowerService.show(borrower.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("borrowInfo",borrowInfo);
        map.put("borrower",borrowerDetailVO);
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {
        Long borrowInfoId = borrowInfoApprovalVO.getId();
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoId);
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus());
        baseMapper.updateById(borrowInfo);

        if(borrowInfoApprovalVO.getStatus().intValue() == BorrowInfoStatusEnum.CHECK_OK.getStatus()){
            // 创建标的
            lendService.createLend(borrowInfoApprovalVO, borrowInfo);
        }
    }

}
