package com.happy.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.happy.easyexcel.dto.ExcelStudentDTO;
import com.happy.easyexcel.listener.ExcelStudentDTOListener;
import org.junit.Test;

/**
 * @author LeiJJ
 * @date 2021-11-01 19:51
 */
public class ExcelReadTest {

    @Test
    public void simpleReadXlsx(){
        String fileName = "d:/desktop/simpleWrite.xlsx";

        EasyExcel.read(fileName, ExcelStudentDTO.class,
                new ExcelStudentDTOListener()).sheet().doRead();
    }
}
