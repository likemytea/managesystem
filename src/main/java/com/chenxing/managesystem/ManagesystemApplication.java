package com.chenxing.managesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@MapperScan("com.chenxing.managesystem.mapper") // 将项目中对应的mapper类
public class ManagesystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagesystemApplication.class, args);
	}
}
