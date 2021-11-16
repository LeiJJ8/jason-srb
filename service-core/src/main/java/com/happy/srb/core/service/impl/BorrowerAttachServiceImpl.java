package com.happy.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.srb.core.pojo.entity.BorrowerAttach;
import com.happy.srb.core.mapper.BorrowerAttachMapper;
import com.happy.srb.core.pojo.vo.BorrowerAttachVO;
import com.happy.srb.core.service.BorrowerAttachService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class BorrowerAttachServiceImpl extends ServiceImpl<BorrowerAttachMapper, BorrowerAttach> implements BorrowerAttachService {

    @Override
    public List<BorrowerAttachVO> selectBorrowerAttachVOList(Long id) {
        QueryWrapper<BorrowerAttach> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("borrower_id",id);
        List<BorrowerAttach> borrowerAttachList = baseMapper.selectList(queryWrapper);

        List<BorrowerAttachVO> borrowerAttachVOList = new ArrayList<>();
        borrowerAttachList.forEach(borrowerAttach -> {
            BorrowerAttachVO borrowerAttachVO = new BorrowerAttachVO();
            borrowerAttachVO.setImageUrl(borrowerAttach.getImageUrl());
            borrowerAttachVO.setImageType(borrowerAttach.getImageType());

            borrowerAttachVOList.add(borrowerAttachVO);
        });

        return borrowerAttachVOList;
    }
}
