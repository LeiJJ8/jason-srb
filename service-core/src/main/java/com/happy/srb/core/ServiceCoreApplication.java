package com.happy.srb.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author LJJ
 * @date 2021-10-27 20:02
 */
@CrossOrigin
@SpringBootApplication
@MapperScan("com.happy.srb.core.mapper")
@ComponentScan({"com.happy.srb","com.happy.common"})
public class ServiceCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCoreApplication.class,args);
    }
}
