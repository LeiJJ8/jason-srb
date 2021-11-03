package com.happy.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.happy.easyexcel.dto.ExcelStudentDTO;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author LeiJJ
 * @date 2021-11-01 18:48
 */
public class ExcelWriteTest {

    @Test
    public void simpleWriteXlsx(){
        String fileName = "d:/desktop/simpleWrite.xlsx";
        EasyExcel.write(fileName, ExcelStudentDTO.class).sheet("模板").doWrite(data());
    }

    private List<ExcelStudentDTO> data(){
        List<ExcelStudentDTO> list = new ArrayList<>();

        for (int i = 0; i < 65535; i++) {
            ExcelStudentDTO data = new ExcelStudentDTO();
            data.setName("胡桃"+i);
            data.setBirthday(new Date());
            data.setSalary(6666.66);
            list.add(data);
        }
        return list;
    }
}
