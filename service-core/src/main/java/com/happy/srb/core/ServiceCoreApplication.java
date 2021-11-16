package com.happy.srb.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LJJ
 * @date 2021-10-27 20:02
 */
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.happy.srb.core.mapper")
@ComponentScan({"com.happy.srb","com.happy.common"})
public class ServiceCoreApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceCoreApplication.class,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
