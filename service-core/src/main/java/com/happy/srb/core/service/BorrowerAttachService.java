package com.happy.srb.core.service;

import com.happy.srb.core.pojo.entity.BorrowerAttach;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.srb.core.pojo.vo.BorrowerAttachVO;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    List<BorrowerAttachVO> selectBorrowerAttachVOList(Long id);
}
