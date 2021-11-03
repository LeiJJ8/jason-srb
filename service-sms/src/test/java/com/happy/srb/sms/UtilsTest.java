package com.happy.srb.sms;

import com.happy.common.util.RandomUtils;
import com.happy.srb.sms.service.SmsService;
import com.happy.srb.sms.utils.SmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LeiJJ
 * @date 2021-11-03 18:41
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilsTest {

    @Autowired
    private SmsService service;

    @Test
    public void testProperties(){
        System.out.println(SmsProperties.KEY_ID);
        System.out.println(SmsProperties.KEY_SECRET);
        System.out.println(SmsProperties.REGION_Id);
        System.out.println(SmsProperties.SIGN_NAME);
    }

    @Test
    public void testSmsService(){
        //生成验证码
        String code = RandomUtils.getFourBitRandom();
        //组装短信模板参数
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        //发送短信
        service.send("19176587511",SmsProperties.TEMPLATE_CODE,param);
    }
}
