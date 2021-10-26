package com.happy.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LJJ
 * @create 2021-10-25 8:49
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
     public MybatisPlusInterceptor mybatisPlusInterceptor(){
         MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
         interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
         return interceptor;
     }
}
