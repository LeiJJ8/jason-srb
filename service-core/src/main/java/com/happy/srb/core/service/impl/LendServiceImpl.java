package com.happy.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.srb.core.enums.LendStatusEnum;
import com.happy.srb.core.enums.ReturnMethodEnum;
import com.happy.srb.core.mapper.BorrowerMapper;
import com.happy.srb.core.pojo.entity.BorrowInfo;
import com.happy.srb.core.pojo.entity.Borrower;
import com.happy.srb.core.pojo.entity.Lend;
import com.happy.srb.core.mapper.LendMapper;
import com.happy.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.happy.srb.core.pojo.vo.BorrowerDetailVO;
import com.happy.srb.core.service.BorrowerService;
import com.happy.srb.core.service.DictService;
import com.happy.srb.core.service.LendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.srb.core.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private BorrowerService borrowerService;

    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo) {
        Lend lend = new Lend();

        lend.setUserId(borrowInfo.getUserId());
        lend.setBorrowInfoId(borrowInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());//生成编号
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setAmount(borrowInfo.getAmount());
        lend.setPeriod(borrowInfo.getPeriod());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));//从审批对象中获取
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));//从审批对象中获取
        lend.setReturnMethod(borrowInfo.getReturnMethod());
        lend.setLowestAmount(new BigDecimal(100));
        lend.setInvestAmount(new BigDecimal(0));
        lend.setInvestNum(0);
        lend.setPublishDate(LocalDateTime.now());

        //起息日期
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dtf);
        lend.setLendStartDate(lendStartDate);
        //结束日期
        LocalDate lendEndDate = lendStartDate.plusMonths(borrowInfo.getPeriod());
        lend.setLendEndDate(lendEndDate);

        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());//描述

        //平台预期收益
        //        月年化 = 年化 / 12
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        //        平台收益 = 标的金额 * 月年化 * 期数
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        lend.setExpectAmount(expectAmount);

        //实际收益
        lend.setRealAmount(new BigDecimal(0));
        //状态
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        //审核时间
        lend.setCheckTime(LocalDateTime.now());
        //审核人
        lend.setCheckAdminId(1L);

        baseMapper.insert(lend);
    }

    @Override
    public List<Lend> getList() {
        List<Lend> lendList = baseMapper.selectList(null);
        lendList.forEach(lend -> {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
            String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
            lend.getParam().put("returnMethod",returnMethod);
            lend.getParam().put("status",status);
        });
        return lendList;
    }

    @Override
    public Map<String, Object> getLendDetail(Long id) {
        Lend lend = baseMapper.selectById(id);

        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
        String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
        lend.getParam().put("returnMethod",returnMethod);
        lend.getParam().put("status",status);

        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id",lend.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);

        BorrowerDetailVO borrowerDetailVO = borrowerService.show(borrower.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("lend",lend);
        map.put("borrower",borrowerDetailVO);

        return map;
    }

    @Override
    public BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalMonth, Integer returnMethod) {
        BigDecimal interestCount;
        if(returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()){
            interestCount = Amount1Helper.getInterestCount(invest,yearRate,totalMonth);
        }else if (returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()) {
            interestCount = Amount2Helper.getInterestCount(invest, yearRate, totalMonth);
        } else if(returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()) {
            interestCount = Amount3Helper.getInterestCount(invest, yearRate, totalMonth);
        } else {
            interestCount = Amount4Helper.getInterestCount(invest, yearRate, totalMonth);
        }
        return interestCount;
    }
}
