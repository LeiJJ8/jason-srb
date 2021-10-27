package com.happy.srb.core.controller.admin;

import com.happy.srb.core.pojo.entity.IntegralGrade;
import com.happy.srb.core.service.IntegralGradeService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LJJ
 * @create 2021-10-27 21:07
 */
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Autowired
    private IntegralGradeService integralGradeService;

    @GetMapping("/list")
    public List<? extends IntegralGrade> listAll(){
        return integralGradeService.list();
    }
}
