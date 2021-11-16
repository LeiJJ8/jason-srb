package com.happy.srb.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LeiJJ
 * @date 2021-11-05 14:09
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan({"com.happy.srb","com.happy.common"})
public class ServiceOSSApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceOSSApplication.class,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
