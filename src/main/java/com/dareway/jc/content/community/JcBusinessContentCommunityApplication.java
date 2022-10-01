package com.dareway.jc.content.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.dareway.jc.content.community.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class JcBusinessContentCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(JcBusinessContentCommunityApplication.class, args);
    }

}
