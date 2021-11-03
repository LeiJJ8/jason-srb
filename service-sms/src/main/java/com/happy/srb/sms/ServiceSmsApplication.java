package com.happy.srb.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author LeiJJ
 * @date 2021-11-03 18:29
 */
@CrossOrigin
@SpringBootApplication
@ComponentScan({"com.happy.srb","com.happy.common"})
public class ServiceSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class,args);
    }
}
