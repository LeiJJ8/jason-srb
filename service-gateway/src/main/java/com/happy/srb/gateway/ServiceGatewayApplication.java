package com.happy.srb.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author LeiJJ
 * @date 2021-11-09 20:21
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceGatewayApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceGatewayApplication.class,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
