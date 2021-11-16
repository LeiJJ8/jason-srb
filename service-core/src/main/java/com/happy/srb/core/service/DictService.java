package com.happy.srb.core.service;

import com.happy.srb.core.pojo.dto.ExcelDictDTO;
import com.happy.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
public interface DictService extends IService<Dict> {

    void importData(InputStream inputStream);

    List<ExcelDictDTO> exportData();

    List<Dict> listByParentId(Long parentId);

    List<Dict> findByDictCode(String dictCode);

    String getNameByParentDictCodeAndValue(String dictCode, Integer value);
}
