package com.happy.srb.core.service;

import com.happy.srb.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.srb.core.pojo.vo.UserBindVO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface UserBindService extends IService<UserBind> {

    String bind(UserBindVO userBindVO, Long userId);

    void toNotify(Map<String, Object> stringObjectMap);

    String getBindCodeByUserId(Long userId);
}
