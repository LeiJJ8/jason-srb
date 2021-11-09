package com.happy.srb.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LeiJJ
 * @date 2021-11-03 18:29
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan({"com.happy.srb","com.happy.common"})
public class ServiceSmsApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceSmsApplication.class,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
