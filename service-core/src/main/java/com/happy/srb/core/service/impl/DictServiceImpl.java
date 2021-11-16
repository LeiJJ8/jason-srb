package com.happy.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.srb.core.listener.ExcelDictDTOListener;
import com.happy.srb.core.pojo.dto.ExcelDictDTO;
import com.happy.srb.core.pojo.entity.Dict;
import com.happy.srb.core.mapper.DictMapper;
import com.happy.srb.core.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class,new ExcelDictDTOListener(baseMapper)).sheet().doRead();
    }

    @Override
    public List<ExcelDictDTO> exportData() {
        List<Dict> dictList = baseMapper.selectList(null);
        //创建ExcelDictDTO列表，将Dict列表转换成ExcelDictDTO列表
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {

            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        List<Dict> dictList = null;
        try {
            dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if(dictList != null){
                return dictList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dictList = baseMapper.selectList(new QueryWrapper<Dict>().eq("parent_id", parentId));
        dictList.forEach(dict -> {
            //如果有子节点，则是非叶子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });

        try {
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId,dictList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictList;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(queryWrapper);
        List<Dict> dictList = listByParentId(dict.getId());
        return dictList;
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        Dict parentDict = baseMapper.selectOne(queryWrapper);
        if(parentDict == null) return "";

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentDict.getId())
                    .eq("value",value);
        Dict dict = baseMapper.selectOne(queryWrapper);
        if(dict == null) return "";

        return dict.getName();
    }

    /**
     * 判断该节点是否有子节点
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>().eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);

        return count > 0;
    }
}
