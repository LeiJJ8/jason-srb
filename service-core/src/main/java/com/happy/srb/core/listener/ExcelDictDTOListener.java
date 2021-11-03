package com.happy.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.happy.srb.core.mapper.DictMapper;
import com.happy.srb.core.pojo.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LeiJJ
 * @date 2021-11-02 19:47
 */
@Slf4j
@NoArgsConstructor
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    private static final int BATCH_COUNT = 5;
    List<ExcelDictDTO> list = new ArrayList<>();

    private DictMapper dictMapper;

    public ExcelDictDTOListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(ExcelDictDTO excelDictDTO, AnalysisContext analysisContext) {
        log.info("解析到一条数据:" + excelDictDTO);
        list.add(excelDictDTO);
        if(list.size() >= BATCH_COUNT){
            dictMapper.insertBatch(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        dictMapper.insertBatch(list);
        log.info("所有数据解析完成");
    }

}
