package com.happy.srb.core.controller.api;


import com.happy.common.result.Result;
import com.happy.srb.core.pojo.entity.Dict;
import com.happy.srb.core.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author LeiJJ
 * @since 2021-10-27
 */
@RestController
@RequestMapping("/api/core/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> dictList = dictService.findByDictCode(dictCode);
        return Result.ok().data("dictList",dictList);
    }
}

