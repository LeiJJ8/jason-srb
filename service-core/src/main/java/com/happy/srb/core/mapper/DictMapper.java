package com.happy.srb.core.mapper;

import com.happy.srb.core.pojo.dto.ExcelDictDTO;
import com.happy.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */

public interface DictMapper extends BaseMapper<Dict> {

    void insertBatch(@Param("excelDictDTOList") List<ExcelDictDTO> excelDictDTOList);
}
