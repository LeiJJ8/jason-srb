package com.happy.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.srb.core.enums.BorrowerStatusEnum;
import com.happy.srb.core.enums.IntegralEnum;
import com.happy.srb.core.mapper.*;
import com.happy.srb.core.pojo.entity.*;
import com.happy.srb.core.pojo.vo.BorrowerApprovalVO;
import com.happy.srb.core.pojo.vo.BorrowerAttachVO;
import com.happy.srb.core.pojo.vo.BorrowerDetailVO;
import com.happy.srb.core.pojo.vo.BorrowerVO;
import com.happy.srb.core.service.BorrowerAttachService;
import com.happy.srb.core.service.BorrowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.srb.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerAttachService borrowerAttachService;

    @Resource
    private UserIntegralMapper userIntegralMapper;

    @Resource
    private IntegralGradeMapper integralGradeMapper;

    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);

        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO,borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        baseMapper.insert(borrower);

        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });

        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Integer getBorrowerStatus(Long userId) {
        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        Borrower borrower = baseMapper.selectOne(queryWrapper);
        if(borrower == null){
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
        return borrower.getStatus();
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {
        if(StringUtils.isEmpty(keyword)){
            return baseMapper.selectPage(pageParam,null);
        }

        QueryWrapper<Borrower> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",keyword)
                    .or()
                    .like("id_card",keyword)
                    .or()
                    .like("mobile",keyword)
                    .orderByDesc("id");

        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public BorrowerDetailVO show(Long id) {
        Borrower borrower = baseMapper.selectById(id);

        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower,borrowerDetailVO);

        borrowerDetailVO.setMarry(borrower.getMarry()?"是":"否");
        borrowerDetailVO.setSex(borrower.getSex() == 1?"男":"女");

        String education = dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation());
        String industry = dictService.getNameByParentDictCodeAndValue("moneyUse", borrower.getIndustry());
        String income = dictService.getNameByParentDictCodeAndValue("income", borrower.getIncome());
        String returnSource = dictService.getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource());
        String contactsRelation = dictService.getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation());

        borrowerDetailVO.setEducation(education);
        borrowerDetailVO.setIndustry(industry);
        borrowerDetailVO.setIncome(income);
        borrowerDetailVO.setReturnSource(returnSource);
        borrowerDetailVO.setContactsRelation(contactsRelation);

        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);

        List<BorrowerAttachVO> borrowerAttachVOList = borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);

        return borrowerDetailVO;
    }

    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {
        // 修改借款人认证状态
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        Borrower borrower = baseMapper.selectById(borrowerId);
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);

        Long userId = borrower.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);

        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);

        int resultIntegral = userInfo.getIntegral() + userIntegral.getIntegral();
        if(borrowerApprovalVO.getIsCarOk()){
            resultIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
        }

        if(borrowerApprovalVO.getIsHouseOk()) {
            resultIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
        }

        if(borrowerApprovalVO.getIsCarOk()) {
            resultIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
        }

        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfo.setIntegral(resultIntegral);
        userInfoMapper.updateById(userInfo);
    }
}
