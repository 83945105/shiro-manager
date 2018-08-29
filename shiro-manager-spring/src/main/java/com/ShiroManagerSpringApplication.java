package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 白超
 */
@EnableZuulProxy
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "com.global.conf",
        "com.*.service",
        "com.*.controller"
})
public class ShiroManagerSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroManagerSpringApplication.class, args);
    }
}
