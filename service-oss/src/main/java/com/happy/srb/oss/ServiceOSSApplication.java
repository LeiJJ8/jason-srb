package com.happy.srb.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author LeiJJ
 * @date 2021-11-05 14:09
 */
@CrossOrigin
@SpringBootApplication
@ComponentScan({"com.happy.srb","com.happy.common"})
public class ServiceOSSApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOSSApplication.class,args);
    }
}
